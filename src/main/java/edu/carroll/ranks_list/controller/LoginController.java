package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.form.LoginForm;
import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.repository.UserRepository;
import edu.carroll.ranks_list.service.UserService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UserRepository userRepo;
    private final UserService userService;

    public LoginController(UserRepository userRepo, UserService userService) {
        this.userRepo = userRepo;
        this.userService = userService;
    }

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