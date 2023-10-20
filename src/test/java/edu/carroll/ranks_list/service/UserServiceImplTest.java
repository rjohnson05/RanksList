package edu.carroll.ranks_list.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;


import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@Transactional
public class UserServiceImplTest {
    private static final String username = "testuser";
    private static final String password = "testpass";

    @Autowired
    private UserService userService;


    @BeforeEach
    public void beforeTest() {
        assertNotNull("userService must be injected", userService);

    }

    @Test
    public void validateUserSuccess(){
        userService.createUser(username, password);
        assertTrue("validateUserSuccess: Should pass with a new user validated returning true", userService.validateUser(username, password));
    }

    @Test
    public void validateUserSuccessTest() {
        userService.createUser(username, password);
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