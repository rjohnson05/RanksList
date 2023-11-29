package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

/**
 * Service class for users. Contains all business logic relating to logins and users.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepo;

    /**
     * Constructor for the User Service, creating the service with a User Repo.
     *
     * @param userRepo Repository for users
     */
    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     *
     * @param username String containing the username supplied for verification
     * @param password String containing the password supplied for verification
     * @return true if data exists and matches what is on record; false otherwise
     */
    @Override
    public boolean validateUser(String username, String password) {
        log.info("validateUser: user '{}' attempted login", username);

        // Always do the lookup in a case-insensitive manner (lower-casing the data).
        List<User> users = userRepo.findByUsernameIgnoreCase(username);

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly.
        if (users.size() != 1) {
            log.info("validateUser: found {} users", users.size());
            return false;
        }

        User user = users.get(0);
        byte[] hash = Base64.getDecoder().decode(user.getPassword());
        byte[] salt = Arrays.copyOfRange(hash, 0, 20);

        // Create a temporary user that stores the password with the same salted hash algorithm as the real user to
        // compare the password hashes.
        User tempUser = new User(username, password, salt);
        if (!user.equals(tempUser)) {
            log.info("validateUser: password !match");
            return false;
        }

        // User exists, and the provided password matches the password in the database.
        log.info("validateUser: successful login for {}", username);
        return true;
    }

    /**
     * Creates a new User object and saves it to the database.
     *
     * @param username String representing the desired username for the new User object
     * @param password String representing the desired password for the new User object
     * @return true if a new User is successfully created and saved to the database; false otherwise
     */
    public boolean createUser(String username, String password) {
        if (userRepo.findByUsernameIgnoreCase(username).isEmpty()) {
            User user = new User(username, password);
            userRepo.save(user);
            log.info("Registered New User: " + username);
            return true;
        }
        log.debug("Attempted registration for already existing user");
        return false;
    }

    /**
     * Updates the password for the user.
     *
     * @param userId the ID of the user to set
     * @param currentPassword the user's current unhashed password
     * @param newPassword the unhashed password to set
     */
    public boolean updatePassword(int userId, String currentPassword, String newPassword){
        // Makes sure the designated user exists
        if (!userRepo.existsById(userId)) {
            log.debug("Unsuccessful attempt to star ad due to invalid ID");
            return false;
        }

        // Make sure the given current password is valid
        User user = userRepo.getReferenceById(userId);
        // Compare the current password and the attempted new one to determine if changes were made
        byte[] hash = Base64.getDecoder().decode(user.getPassword());
        byte[] salt = Arrays.copyOfRange(hash, 0, 20);

        // Create a temporary user that stores the password with the same salted hash algorithm as the real user to
        // compare the password hashes.
        User tempUser = new User(user.getUsername(), currentPassword, salt);
        if (!user.equals(tempUser)) {
            log.debug("Attempt to edit ad unsuccessful due to incorrect password");
            return false;
        }

        // Makes sure the new password is different from the current password
        // Create a temporary user that stores the password with the same salted hash algorithm as the real user to
        // compare the password hashes.
        tempUser = new User(user.getUsername(), newPassword, salt);
        if (user.equals(tempUser)) {
            log.debug("Attempt to edit ad unsuccessful due to no changes being made");
            return false;
        }

        log.info("Setting new password for user {}", user);
        user.setPassword(newPassword);
        userRepo.save(user);
        return true;
    }

    /**
     * Returns a list of User object with the specified username.
     *
     * @param username String representing the desired username of the user
     * @return list of Users with the specified username
     */
    @Override
    public List<User> findByUsernameIgnoreCase(String username) {
        return userRepo.findByUsernameIgnoreCase(username);
    }

    /**
     * Returns the User object with the specified ID
     *
     * @param id Integer representing the ID number of the desired User object
     * @return User object with the specified ID
     */
    @Override
    public User getReferenceById(Integer id) {
        return userRepo.getReferenceById(id);
    }

    ;
}