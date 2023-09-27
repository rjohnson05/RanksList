package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.repository.UserRepository;
import edu.carroll.ranks_list.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * API for HTTP requests regarding user logins. Specifies how to handle HTTP requests that involve user login data.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@RestController
@CrossOrigin("http://localhost:3000")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UserRepository userRepo;
    private final UserService userService;

    /**
     * Constructor for the Login Controller. Creates a service for the login business logic.
     *
     * @param userRepo
     * @param userService userService contains all logic related to login data
     */
    public LoginController(UserRepository userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

    /**
     * Registers a new user and adds their credentials to the database.
     *
     * @param newUser User object containing login credentials
     * @return True if the credentials do not match credentials from another user; False otherwise
     */
    @PostMapping("/register")
    public boolean createUser(@RequestBody User newUser) {
        if (userRepo.findByUsernameIgnoreCase(newUser.getUsername()).isEmpty()) {
            User user = new User(newUser.getUsername(),newUser.getPassword());
            userRepo.save(user);
            newUser.setHashedPassword(newUser.getPassword());
            log.info("New Password: {}", newUser.getPassword());
            log.info("Registered New User: " + newUser.getUsername());
            return true;
        }
        log.debug("Attempted registration for already existing user");
        return false;
    }

    /**
     * Determines if the login credential information matches a current user in the database.
     * @param potentialUser     User object containing a username and password being used to try to log in
     * @return True if the credentials match a user in the database; False otherwise
     */
    @PostMapping("/login")
    public boolean loginPost(@RequestBody User potentialUser) {
//        if (potentialUser.hasErrors()) {
//            log.debug("Attempted login");
//            return "login";
//        }
        if (!userService.validateUser(potentialUser.getUsername(), potentialUser.getPassword())) {
            log.debug(potentialUser.getUsername(), " attempted to log in");
            return false;
        }
        log.info(potentialUser.getUsername(), " successfully logged in");
        return true;
    }
}