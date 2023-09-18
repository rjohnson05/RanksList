package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.repository.UserRepository;
import edu.carroll.ranks_list.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
public class UserServiceImplTest {
    private static final String username = "testuser";
    private static final String password = "testpass";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    private User fakeUser = new User(username, password);

    @BeforeEach
    public void beforeTest() {
        assertNotNull("userRepository must be injected", userRepo);
        assertNotNull("userService must be injected", userService);

        // Ensure dummy record is in the DB
        final List<User> users = userRepo.findByUsernameIgnoreCase(username);
        if (users.isEmpty())
            userRepo.save(fakeUser);
    }

    @Test
    public void validateUserSuccessTest() {
        assertTrue("validateUserSuccessTest: should succeed using the same user/pass info", userService.validateUser(username, password));
    }

    @Test
    public void validateUserExistingUserInvalidPasswordTest() {
        assertFalse("validateUserExistingUserInvalidPasswordTest: should fail using a valid user, invalid pass", userService.validateUser(username, password + "extra"));
    }

    @Test
    public void validateUserInvalidUserValidPasswordTest() {
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(username + "not", password));
    }

    @Test
    public void validateUserInvalidUserInvalidPasswordTest() {
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(username + "not", password + "extra"));
    }
}