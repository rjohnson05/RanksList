package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:3000")
public class LoginController {
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public boolean createUser(@RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    /**
     * Determines if the login credential information matches a current user in the database.
     * @param potentialUser     User object containing a username and password being used to try to log in
     * @return True if the credentials match a user in the database; False otherwise
     */
    @PostMapping("/login")
    public boolean loginPost(@RequestBody User potentialUser) {
        if (!userService.validateUser(potentialUser.getUsername(), potentialUser.getPassword())) {
            log.debug(potentialUser.getUsername(), " attempted to log in");
            return false;
        }
        log.info(potentialUser.getUsername(), " successfully logged in");
        return true;
    }
}