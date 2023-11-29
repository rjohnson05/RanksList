package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@Transactional
public class UserServiceImplTest {
    private static final String username = "testuser";
    private static final String password = "Password!";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepo;

    @BeforeEach
    public void beforeTest() {
        assertEquals("Size of user database should be 0 when we start our tests",0, userRepo.findAll().size());
        assertTrue("User was not created correctly", userService.createUser(username, password));
    }

    // testing validate user
    @Test
    public void validateUserSuccess() {
        assertTrue("validateUserSuccess: Should pass with a new user validated returning true", userService.validateUser(username, password));
        assertEquals("validateUserSuccess: Should pass with username being equal to what it was set to", username, userService.findByUsernameIgnoreCase(username).get(0).getUsername());
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        User user = users.get(0);
        byte[] hash = Base64.getDecoder().decode(user.getPassword());
        byte[] salt = Arrays.copyOfRange(hash, 0, 20);
        User tempUser = new User(username, password, salt);
        assertEquals("validateUserSuccess: Should pass with password being equal to what it was set to", userService.findByUsernameIgnoreCase(username).get(0).getPassword(), tempUser.getPassword());

    }

    // testng validate user with two users in the database
    @Test
    public void validateUserTwoUsersSuccess() {
        assertTrue("validateUserSuccess: Should pass with a new user created returning true", userService.createUser(username + "extra", password));
        assertTrue("validateUserSuccess: Should pass with a new user validated returning true", userService.validateUser(username, password));
        assertEquals("validateUserSuccess: Should pass with username being equal to what it was set to", username, userService.findByUsernameIgnoreCase(username).get(0).getUsername());
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        User user = users.get(0);
        byte[] hash = Base64.getDecoder().decode(user.getPassword());
        byte[] salt = Arrays.copyOfRange(hash, 0, 20);
        User tempUser = new User(username, password, salt);
        assertEquals("validateUserSuccess: Should pass with password being equal to what it was set to", userService.findByUsernameIgnoreCase(username).get(0).getPassword(), tempUser.getPassword());
    }

    // testing validate user with multiple users in the database
    @Test
    public void validateUserMultipleUsersSuccess() {
        for (int i = 0; i < 10; i++ ){
            assertTrue("validateUserMultipleUsersSuccess: Should pass with a new user created returning true", userService.createUser(username + i, password));
        }
        assertTrue("validateUserMultipleUsersSuccess: Should pass with a new user validated returning true", userService.validateUser(username, password));
        assertEquals("validateUserMultipleUsersSuccess: Should pass with username being equal to what it was set to", username, userService.findByUsernameIgnoreCase(username).get(0).getUsername());
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        User user = users.get(0);
        byte[] hash = Base64.getDecoder().decode(user.getPassword());
        byte[] salt = Arrays.copyOfRange(hash, 0, 20);
        User tempUser = new User(username, password, salt);
        assertEquals("validateUserMultipleUsersSuccess: Should pass with password being equal to what it was set to", userService.findByUsernameIgnoreCase(username).get(0).getPassword(), tempUser.getPassword());
    }

    // testing validate user
    @Test
    public void validateUserExistingUserInvalidPasswordTest() {
        assertFalse("validateUserExistingUserInvalidPasswordTest: should fail using a valid user, invalid pass", userService.validateUser(username, password + "extra"));
        assertEquals("validateUserExistingUserInvalidPasswordTest: should pass finding a valid user with valid username", username, userService.findByUsernameIgnoreCase(username).get(0).getUsername());
        List<User> users = userRepo.findByUsernameIgnoreCase(username);
        User user = users.get(0);
        byte[] hash = Base64.getDecoder().decode(user.getPassword());
        byte[] salt = Arrays.copyOfRange(hash, 0, 20);
        User tempUser = new User(username, password+"extra", salt);
        assertNotEquals("validateUserExistingUserInvalidPasswordTest: Should pass with password not being equal to the invalid password", userService.findByUsernameIgnoreCase(username).get(0).getPassword(), tempUser.getPassword());
    }

    // testing validate user
    @Test
    public void validateUserInvalidUserValidPasswordTest() {
        assertFalse("validateUserInvalidUserValidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(username + "not", password));
        assertNotEquals("validateUserInvalidUserValidPasswordTest: should pass not finding a user with a username not in the database", username + "not", userService.findByUsernameIgnoreCase(username).get(0).getUsername());
    }

    // testing validate user
    @Test
    public void validateUserInvalidUserInvalidPasswordTest() {
        assertFalse("validateUserInvalidUserInvalidPasswordTest: should fail using an invalid user, valid pass", userService.validateUser(username + "not", password + "extra"));
        assertNotEquals("validateUserInvalidUserInvalidPasswordTest: should pass not finding a user with a username not in the database", username + "not", userService.findByUsernameIgnoreCase(username).get(0).getUsername());
    }

    // testing create user
    @Test
    public void createUserSuccess() {
        User user = userService.findByUsernameIgnoreCase(username).get(0);
        assertEquals("createUserSuccess: should pass with size of user list being one", userService.findByUsernameIgnoreCase(username).size(), 1);
        assertEquals("createUserSuccess: should pass with username being equal to what it was set to",username, user.getUsername());
        byte[] hash = Base64.getDecoder().decode(user.getPassword());
        byte[] salt = Arrays.copyOfRange(hash, 0, 20);
        User tempUser = new User(username, password, salt);
        assertEquals("createUserSuccess: Should pass with password being equal to what it was set to", userService.findByUsernameIgnoreCase(username).get(0).getPassword(), tempUser.getPassword());
    }

    // testing create user
    @Test
    public void createTwoUserSuccess() {
        assertTrue("createTwoUserSuccess: should pass with user being created", userService.createUser(username + "extra", password + "extra"));
        User user = userService.findByUsernameIgnoreCase(username).get(0);
        assertEquals("createTwoUserSuccess: should pass with size of user list being two", userRepo.findAll().size(), 2);

        byte[] hash = Base64.getDecoder().decode(user.getPassword());
        byte[] salt = Arrays.copyOfRange(hash, 0, 20);
        User tempUser = new User(username, password, salt);
        assertEquals("createTwoUserSuccess: Should pass with password being equal to what it was set to", userService.findByUsernameIgnoreCase(username).get(0).getPassword(), tempUser.getPassword());
        assertEquals("createTwoUserSuccess: should pass with username being equal to what it was set to",username, user.getUsername());

        User user2 = userService.findByUsernameIgnoreCase(username + "extra").get(0);
        assertEquals("createTwoUserSuccess: should pass with username being equal to what it was set to",username + "extra", user2.getUsername());
    }

    // testing create user
    @Test
    public void createMultipleUserSuccess() {
        for (int i = 0; i < 10; i++){
            assertTrue("createMultipleUserSuccess: should pass with user being created", userService.createUser(username + i, password));
        }
        User user = userService.findByUsernameIgnoreCase(username).get(0);
        assertEquals("createMultipleUserSuccess: should pass with size of user list being 11", userRepo.findAll().size(), 11);
        assertEquals("createMultipleUserSuccess: should pass with username being equal to what it was set to",username, user.getUsername());

        for (int i = 0; i < 10; i++){
            User specificUser = userService.findByUsernameIgnoreCase(username + i).get(0);
            assertEquals("createMultipleUserSuccess: should pass with username being equal to what it was set to",username + i, specificUser.getUsername());
        }
    }

    // testing update password
    @Test
    public void updatePasswordTest(){
        int userId = userRepo.findByUsernameIgnoreCase(username).get(0).getId();
        assertTrue("UpdatePasswordTest: Should pass with the password being updated", userService.updatePassword(userId, password, password+"new"));
        User user = userService.getReferenceById(userId);
        byte[] hash = Base64.getDecoder().decode(user.getPassword());
        byte[] salt = Arrays.copyOfRange(hash, 0, 20);
        User tempUser = new User(username, password+"new", salt);
        assertEquals("UpdatePasswordTest: Should pass with password being equal to what it was set to", userService.findByUsernameIgnoreCase(username).get(0).getPassword(), tempUser.getPassword());

    }

    // testing find user by username
    @Test
    public void findByUsernameIgnoreCase() {
        userService.createUser("testUser", "testPassword");
        assertEquals("findByUsernameIgnoreCase: should pass with size of user list being 0", userService.findByUsernameIgnoreCase("test").size(), 0);
        assertEquals("findByUsernameIgnoreCase: should pass with username being equal to what it was set to", userService.findByUsernameIgnoreCase(username).get(0).getUsername(), username);
    }

    // testing get reference by id
    @Test
    public void getReferenceByIdTest() {
        int id = userService.findByUsernameIgnoreCase(username).get(0).getId();
        assertTrue("getReferenceByIdTest: should pass with user id searched equal to the username found", userService.getReferenceById(id).getUsername().equals(username));
    }

}