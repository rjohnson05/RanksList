package edu.carroll.ranks_list.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.Set;

/**
 * Model for advertisements. Contains all information regarding any given ad.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@Entity
@Table(name = "`user`")
public class User {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "password", nullable = false)
    String password;

    @OneToMany(mappedBy="user")
    private Set<Ad> ads;

    /**
     * Constructor for the User model. Creates a default User object with no information.
     */
    public User() {
    }

    /**
     * Constructor for the User model. Creates a default User object with a username and password.
     *
     * @param username String object containing the username associated with the user
     * @param  rawPassword String object containing the password directly supplied by the user, without any manipulation
     */
    public User(String username, String rawPassword) {
        this.username = username;
        setHashedPassword(rawPassword);
    }

    /**
     * Returns the ID of the user.
     *
     * @return Integer object representing the ID of the user
     */
    public Integer getId() {
        return id;
    }


    /**
     * Sets the ID of the user.
     *
     * @param id Integer object representing the ID of the user
     */
    public void setId(Integer id) {
        this.id = id;
    }


    /**
     * Returns the username of the user.
     *
     * @return String object containing the username of the user
     */
    @NonNull
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username String object containing the username of the user
     */
    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    /**
     * Returns the password for the user.
     *
     * @return String object containing the password for the user
     */
    @NonNull
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the user.
     *
     * @param password String object containing the password for the user
     */
    public void setPassword(@NonNull String password) {
        this.password = password;
    }

    /**
     * Sets the hashed password to the password. NEEDS TO BE REDONE XXX
     * @param rawPassword
     */
    public void setHashedPassword(String rawPassword) {
        // XXX - This should *NEVER* be done in a real project
        this.password = Integer.toString(rawPassword.hashCode());
    }

    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";

    /**
     *
     * @return String with login, username, and hidden password
     */
    public String toString() {
        return "User #" + id + " [" + EOL +
                TAB + "Username: " + username + EOL +
                TAB + "Password: ********" + EOL +
                "]" + EOL;
    }

    /**
     *
     * @param o
     * @return True if objects are equal to eachother, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final User user = (User)o;
        return username.equals(user.username) && password.equals(user.password);
    }

    /**
     * NEEDS TO BE REDONE
     * @return java hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}