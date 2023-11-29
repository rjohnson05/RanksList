package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.form.LoginForm;
import edu.carroll.ranks_list.form.RegistrationForm;
import edu.carroll.ranks_list.repository.UserRepository;
import edu.carroll.ranks_list.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
     * @param userRepo
     */
    public LoginController(UserService userService, UserRepository userRepo) {
        this.userService = userService;
    }

    /**
     * Registers a new user and adds their credentials to the database.
     *
     * @param registrationForm contains the data to be used to create a new user
     * @return true if the user is successfully created, having no matching credentials already present in the DB;
     * false otherwise
     */
    @PostMapping("/register")
    public boolean createUser(@RequestBody RegistrationForm registrationForm) {
        return userService.createUser(registrationForm.getUsername(), registrationForm.getPassword());
    }

    /**
     * Determines if the login credential information matches a current user in the database.
     *
     * @param loginForm Contains the data used for login verification
     * @param request   HttpServletRequest object that allows access to parameters of an HTTP request
     * @return true if the credentials match a user in the database; false otherwise
     */
    @PostMapping("/login")
    public boolean loginPost(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        if (!userService.validateUser(loginForm.getUsername(), loginForm.getPassword())) {
            log.debug("Unsuccessful attempt to sign in with {} {}",loginForm.getUsername(), loginForm.getPassword());
            return false;
        }
        Integer currentUserId = userService.findByUsernameIgnoreCase(loginForm.getUsername()).get(0).getId();
        HttpSession session = request.getSession();
        session.setAttribute("userID", currentUserId.toString());
        log.debug("Session UserID set as " + session.getAttribute("userID"));
        return true;
    }

    /**
     * Updates a user's password to a given value.
     *
     * @param loginForm Contains the data used to edit the password
     * @param request   HttpServletRequest object that allows access to parameters of an HTTP request
     * @return true if the credentials match a user in the database; false otherwise
     */
    @PutMapping("/edit_password")
    public boolean updatePassword(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        HttpSession session = request.getSession();
        int currentUserId = Integer.parseInt((String) session.getAttribute("userID"));
        log.info("Current Password: {}, New Password: {}", loginForm.getPassword(), loginForm.getNewPassword());
        if (!userService.updatePassword(currentUserId, loginForm.getPassword(), loginForm.getNewPassword())) {
            return false;
        }
        log.info("Updated password for user:{}", loginForm.getUsername());
        return true;
    }
}