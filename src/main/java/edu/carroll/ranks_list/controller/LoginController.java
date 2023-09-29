package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.form.LoginForm;
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
    public boolean createUser(@RequestBody String username, String password) {
        return userService.createUser(username, password);
    }

    /**
     * Determines if the login credential information matches a current user in the database.
     * @param loginForm
     * @return True if the credentials match a user in the database; False otherwise
     */
    @PostMapping("/login")
    public boolean loginPost(@RequestBody LoginForm loginForm) {
        if (!userService.validateUser(loginForm.getUsername(), loginForm.getPassword())) {
            System.out.println(loginForm.getUsername() + " attempted to log in");
            log.debug(loginForm.getUsername(), " attempted to log in");
            return false;
        }
        log.info(loginForm.getUsername(), " successfully logged in");
        return true;
    }
}