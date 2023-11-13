package edu.carroll.ranks_list.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    // testing validate user
    @Test
    public void validateUserSuccess(){
        userService.createUser(username, password);
        assertTrue("validateUserSuccess: Should pass with a new user validated returning true", userService.validateUser(username, password));
    }


    // testing validate user
    @Test
    public void validateUserExistingUserInvalidPasswordTest() {
        assertFalse("validateUserExistingUserInvalidPasswordTest: should fail using a valid user, invalid pass", userService.validateUser(username, password + "extra"));
    }

    // testing validate user
    @Test
    public void validateUserInvalidUserValidPasswordTest() {
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(username + "not", password));
    }

    // testing validate user
    @Test
    public void validateUserInvalidUserInvalidPasswordTest() {
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(username + "not", password + "extra"));
    }

    // testing create user
    @Test
    public void createUserSuccess() {
        assertTrue("createUserSuccess: should pass with user being created", userService.createUser("testUser", "testPassword"));
        assertTrue("createUserSuccess: should pass with size of user list being one", userService.findByUsernameIgnoreCase("testUser").size() == 1);
        assertTrue("createUserSuccess: should pass with username being equal to what it was set to", userService.findByUsernameIgnoreCase("testUser").get(0).getUsername().equals("testUser"));
    }

    // testing find user by username
    @Test
    public void findByUsernameIgnoreCase() {
        userService.createUser("testUser", "testPassword");
        assertTrue("findByUsernameIgnoreCase: should pass with size of user list being 0", userService.findByUsernameIgnoreCase("test").size() == 0);
        assertTrue("findByUsernameIgnoreCase: should pass with username being equal to what it was set to", userService.findByUsernameIgnoreCase("testUser").get(0).getUsername().equals("testUser"));
    }

    // testing get reference by id
    @Test
    public void getReferenceByIdTest() {
        userService.createUser("testUser", "testPassword");
        int id = userService.findByUsernameIgnoreCase("testUser").get(0).getId();
        assertTrue("getReferenceByIdTest: should pass with user id searched equal to the username found", userService.getReferenceById(id).getUsername().equals("testUser"));
    }


}