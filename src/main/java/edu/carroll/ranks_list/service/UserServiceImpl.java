package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for users.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     *
     * @return true if data exists and matches what's on record, false otherwise
     */
    @Override
    public boolean validateUser(String username, String password) {
        log.debug("validateUser: user '{}' attempted login", username);

        // Always do the lookup in a case-insensitive manner (lower-casing the data).
        List<User> users = userRepo.findByUsernameIgnoreCase(username);

        // We expect 0 or 1, so if we get more than 1, bail out as this is an error we don't deal with properly.
        if (users.size() != 1) {
            log.debug("validateUser: found {} users", users.size());
            return false;
        }
        User u = users.get(0);
        // XXX - Using Java's hashCode is wrong on SO many levels, but is good enough for demonstration purposes.
        // NEVER EVER do this in production code!
        final String userProvidedHash = Integer.toString(password.hashCode());
        if (!u.getPassword().equals(userProvidedHash)){
            log.debug("validateUser: password !match");
            return false;
        }

        // User exists, and the provided password matches the hashed password in the database.
        log.info("validateUser: successful login for {}", username);
        return true;
    }

    public boolean createUser(String username, String password){
        if (userRepo.findByUsernameIgnoreCase(username).isEmpty()) {
            User user = new User(username,password);
            userRepo.save(user);
            user.setHashedPassword(password);
            log.info("Set Password: ****");
            log.info("Registered New User: " + username);
            return true;
        }
        log.debug("Attempted registration for already existing user");
        return false;
    }

    @Override
    public List<User> findByUsernameIgnoreCase(String username) {
        return userRepo.findByUsernameIgnoreCase(username);
    }

    @Override
    public User getReferenceById(Integer id) {
        return userRepo.getReferenceById(id);
    };
}