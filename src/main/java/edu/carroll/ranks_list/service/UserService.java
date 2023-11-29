package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.User;

import java.util.List;

/**
 * Interface for user services. Contains all business methods relating to users.
 *
 * @author Hank Rugg, Ryan Johnson
 */
public interface UserService {

    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     *
     * @param username String containing the username supplied for verification
     * @param password String containing the password supplied for verification
     * @return true if data exists and matches what is on record; false otherwise
     */
    boolean validateUser(String username, String password);

    /**
     * Creates a new User object and saves it to the database.
     *
     * @param username String representing the desired username for the new User object
     * @param password String representing the desired password for the new User object
     * @return true if a new User is successfully created and saved to the database; false otherwise
     */
    boolean createUser(String username, String password);

    /**
     * Updates the password for the user
     *
     * @param userId the ID of the user to set
     * @param currentPassword the user's current unhashed password
     * @param newPassword the unhashed password to set
     */
    boolean updatePassword(int userId, String currentPassword, String newPassword);

    /**
     * Returns a list of User object with the specified username.
     *
     * @param username String representing the desired username of the user
     * @return list of Users with the specified username
     */
    List<User> findByUsernameIgnoreCase(String username);

    /**
     * Returns the User object with the specified ID
     *
     * @param id Integer representing the ID number of the desired User object
     * @return User object with the specified ID
     */
    User getReferenceById(Integer id);
}