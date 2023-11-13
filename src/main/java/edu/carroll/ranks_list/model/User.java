package edu.carroll.ranks_list.model;

import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.*;

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

    @OneToMany(mappedBy = "user")
    private List<Ad> ads;

    @OneToMany(mappedBy = "user")
    private List<Goal> goals;

    /**
     * Constructor for the User model. Creates a default User object with no information.
     */
    public User() {
    }

    /**
     * Constructor for the User model. Creates a User object with the provided username and password.
     * Uses the provided salt to hash the password.
     *
     * @param username    String object containing the username associated with the user
     * @param rawPassword String object containing the password directly supplied by the user, without any manipulation
     * @param salt        list of bytes containing the salt to be used for hashing the password
     */
    public User(String username, String rawPassword, byte[] salt) {
        this.username = username;
        setHashedPassword(rawPassword, salt);
    }

    /**
     * Constructor for the User model. Creates a User object with a username and password, using a random salt to hash
     * the password.
     *
     * @param username    String object containing the username associated with the user
     * @param rawPassword String object containing the password directly supplied by the user, without any manipulation
     */
    public User(String username, String rawPassword) {
        this.username = username;

        // Generates a salt for the hash
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[20];
        secureRandom.nextBytes(salt);
        setHashedPassword(rawPassword, salt);
    }

    /**
     * Returns the ID of the user.
     *
     * @return Integer representing the ID of the user
     */
    @NonNull
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
     * Sets the hashed password to the password.
     *
     * @param rawPassword String object containing the password directly supplied by the user, without any manipulation
     * @param salt        list of bytes containing the salt to be used for hashing the password
     */
    public void setHashedPassword(String rawPassword, byte[] salt) {
        try {
            // Creates a hash of the password using the preceding generated salt
            PBEKeySpec spec = new PBEKeySpec(rawPassword.toCharArray(), salt, 1000, 512);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

            byte[] hash = factory.generateSecret(spec).getEncoded();
            // Combines the salt with the hashed password for storage in the DB, allowing the salt to be retrieved for
            // password verification
            byte[] salt_hash = Arrays.copyOf(salt, salt.length + hash.length);
            System.arraycopy(hash, 0, salt_hash, salt.length, hash.length);

            this.password = Base64.getEncoder().encodeToString(salt_hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ignored) {
        }
    }

    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final User user = (User) o;
        return username.equals(user.username) && password.equals(user.password);
    }

    public int hashCode() {
        return Objects.hash();
    }

    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";

    public String toString() {
        return "User #" + id + " [" + EOL +
                TAB + "Username: " + username + EOL +
                TAB + "Password: ********" + EOL +
                "]" + EOL;
    }
}