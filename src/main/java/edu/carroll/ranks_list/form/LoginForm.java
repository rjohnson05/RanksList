package edu.carroll.ranks_list.form;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Form for logging in and verifying users. Contains information necessary for logging in to an account, including username and password.
 *
 * @author Hank Rugg, Ryan Johnson
 */
public class LoginForm {

    @NotNull
    @Size(min = 6, max = 32)
    private String username;

    @NotNull
    @Size(min = 8, max = 16)
    private String password;

    /**
     * Returns the username supplied by the user.
     *
     * @return String containing the username supplied by the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username to that supplied by the user.
     *
     * @param username String containing the username supplied by the user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password supplied by the user.
     *
     * @return String containing the password supplied by the user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password to that supplied by the user.
     *
     * @param password String containing the password supplied by the user
     */
    public void setPassword(String password) {
        this.password = password;
    }
}