package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.form.LoginForm;
import edu.carroll.ranks_list.form.RegistrationForm;
import edu.carroll.ranks_list.repository.UserRepository;
import edu.carroll.ranks_list.service.UserService;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


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
    private final UserRepository userRepo;

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
     * @param username Username of the user
     * @param password Password of the user
     * @return True if the credentials do not match credentials from another user; False otherwise
     */
    @PostMapping("/register")
    public boolean createUser(@RequestBody RegistrationForm registrationForm) {
        return userService.createUser(registrationForm.getUsername(), registrationForm.getPassword());
    }

    /**
     * Determines if the login credential information matches a current user in the database.
     * @param loginForm
     * @return True if the credentials match a user in the database; False otherwise
     */
    @PostMapping("/login")
    public boolean loginPost(@RequestBody LoginForm loginForm, HttpServletRequest request, HttpServletResponse response) {
        if (!userService.validateUser(loginForm.getUsername(), loginForm.getPassword())) {
            log.debug(loginForm.getUsername(), " attempted to log in");
            return false;
        }
        Integer currentUserId = userRepo.findByUsernameIgnoreCase(loginForm.getUsername()).get(0).getId();
        Cookie cookie = new Cookie("userID", currentUserId.toString());
        response.addCookie(cookie);
        log.info("Cookie: " + cookie.getValue());
        cookie.setPath("/");
        cookie.setHttpOnly(false);

        Cookie[] cookies = request.getCookies();
        log.info("Cookies: " + Arrays.toString(cookies));
        return true;
    }
}