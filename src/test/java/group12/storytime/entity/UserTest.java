package group12.storytime.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private UUID userID;
    private String username;
    private String password;
    private String email;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLogin;

    @BeforeEach
    public void setUp() {
        user = new User();
        userID = UUID.randomUUID();
        username = "test_user";
        password = "password123";
        email = "test@example.com";
        registrationDate = LocalDateTime.now().minusDays(1);
        lastLogin = LocalDateTime.now();
    }

    @Test
    public void testDefaultConstructor() {
        User defaultUser = new User();
        assertNull(defaultUser.getUserID());
        assertNull(defaultUser.getUsername());
        assertNull(defaultUser.getPassword());
        assertNull(defaultUser.getEmail());
        assertNull(defaultUser.getRegistrationDate());
        assertNull(defaultUser.getLastLogin());
    }

    @Test
    public void testParameterizedConstructor() {
        User paramUser = new User(username, password, email);
        assertNotNull(paramUser.getUserID());
        assertEquals(username, paramUser.getUsername());
        assertEquals(password, paramUser.getPassword());
        assertEquals(email, paramUser.getEmail());
        assertNotNull(paramUser.getRegistrationDate());
        assertNotNull(paramUser.getLastLogin());
    }

    @Test
    public void testSetAndGetUserID() {
        user.setUserID(userID);
        assertEquals(userID, user.getUserID());
    }

    @Test
    public void testSetAndGetUsername() {
        user.setUsername(username);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testSetAndGetPassword() {
        user.setPassword(password);
        assertEquals(password, user.getPassword());
    }

    @Test
    public void testSetAndGetEmail() {
        user.setEmail(email);
        assertEquals(email, user.getEmail());
    }

    @Test
    public void testSetAndGetRegistrationDate() {
        user.setRegistrationDate(registrationDate);
        assertEquals(registrationDate, user.getRegistrationDate());
    }

    @Test
    public void testSetAndGetLastLogin() {
        user.setLastLogin(lastLogin);
        assertEquals(lastLogin, user.getLastLogin());
    }

    @Test
    public void testToString() {
        user.setUserID(userID);
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setRegistrationDate(registrationDate);
        user.setLastLogin(lastLogin);

        String expected = "User(userID=" + userID + ", username=" + username + ", password=" + password +
                ", email=" + email + ", registrationDate=" + registrationDate + ", lastLogin=" + lastLogin + ")";
        assertEquals(expected, user.toString());
    }
}
