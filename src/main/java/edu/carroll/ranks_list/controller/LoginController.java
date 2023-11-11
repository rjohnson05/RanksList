package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.form.LoginForm;
import edu.carroll.ranks_list.form.RegistrationForm;
import edu.carroll.ranks_list.service.UserService;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * API for HTTP requests regarding user logins. Specifies how to handle HTTP requests that involve user login data.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class LoginController extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    private final UserService userService;

    /**
     * Constructor for the Login Controller. Creates a service for the login business logic.
     *
     * @param userService userService contains all logic related to login data
     */
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Registers a new user and adds their credentials to the database.
     *
     * @return true if the credentials do not match credentials from another user; false otherwise
     */
    @PostMapping("/register")
    public boolean createUser(@RequestBody RegistrationForm registrationForm) {
        return userService.createUser(registrationForm.getUsername(), registrationForm.getPassword());
    }

    /**
     * Determines if the login credential information matches a current user in the database.
     * @param loginForm Contains the data to used for login verification
     * @return true if the credentials match a user in the database; false otherwise
     */
    @PostMapping("/login")
    public boolean loginPost(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        if (!userService.validateUser(loginForm.getUsername(), loginForm.getPassword())) {
            log.debug("Unsuccessful attempt to sign in with (loginForm.getUsername(), loginForm.getPassword())");
            return false;
        }
        Integer currentUserId = userService.findByUsernameIgnoreCase(loginForm.getUsername()).get(0).getId();
        HttpSession session = request.getSession();
        session.setAttribute("userID", currentUserId.toString());
        log.debug("Session UserID set as " + session.getAttribute("userID"));
        return true;
    }
}