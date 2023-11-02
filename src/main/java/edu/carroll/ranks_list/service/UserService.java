package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.form.LoginForm;
import edu.carroll.ranks_list.model.User;

import java.util.List;

public interface UserService {
    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     * @return true if data exists and matches what's on record, false otherwise
     */

    boolean validateUser(String username, String password);

    boolean createUser(String username, String password);

    List<User> findByUsernameIgnoreCase(String username);

    User getReferenceById(Integer id);
}