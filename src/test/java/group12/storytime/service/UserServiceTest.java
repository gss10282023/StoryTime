package group12.storytime.service;

import group12.storytime.entity.User;
import group12.storytime.repository.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        // 初始化测试用户
        testUser = new User();
        testUser.setUserID(UUID.randomUUID());
        testUser.setUsername("test_user");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setEmail("test@example.com");
        testUser.setRegistrationDate(LocalDateTime.now());
        testUser.setLastLogin(LocalDateTime.now());
    }

    @Test
    public void testLogin_Success_WithUsername() {
        when(userMapper.selectUserByUsername("test_user")).thenReturn(testUser);
        User loggedInUser = userService.login("test_user", "password");

        assertNotNull(loggedInUser);
        assertEquals(testUser.getUsername(), loggedInUser.getUsername());
        verify(userMapper).updateLastLogin(eq(testUser.getUserID()), any(LocalDateTime.class));
    }

    @Test
    public void testLogin_Success_WithEmail() {
        when(userMapper.selectUserByEmail("test@example.com")).thenReturn(testUser);
        User loggedInUser = userService.login("test@example.com", "password");

        assertNotNull(loggedInUser);
        assertEquals(testUser.getEmail(), loggedInUser.getEmail());
        verify(userMapper).updateLastLogin(eq(testUser.getUserID()), any(LocalDateTime.class));
    }

    @Test
    public void testLogin_Fail_InvalidPassword() {
        when(userMapper.selectUserByUsername("test_user")).thenReturn(testUser);
        User loggedInUser = userService.login("test_user", "wrong_password");

        assertNull(loggedInUser);
        verify(userMapper, never()).updateLastLogin(any(), any());
    }

    @Test
    public void testLogin_Fail_InvalidUsernameOrEmail() {
        when(userMapper.selectUserByUsername("invalid_user")).thenReturn(null);
        User loggedInUser = userService.login("invalid_user", "password");

        assertNull(loggedInUser);
    }

    @Test
    public void testRegister_Success() throws Exception {
        when(userMapper.selectUserByEmail(testUser.getEmail())).thenReturn(null);
        userService.register(testUser);

        verify(userMapper).insertUser(any(User.class));
    }

    @Test
    public void testRegister_Fail_EmailAlreadyExists() {
        when(userMapper.selectUserByEmail(testUser.getEmail())).thenReturn(testUser);

        Exception exception = assertThrows(Exception.class, () -> userService.register(testUser));
        assertEquals("Email already registered", exception.getMessage());
    }

    @Test
    public void testResetPassword_Success() throws Exception {
        when(userMapper.selectUserByEmail("test@example.com")).thenReturn(testUser);

        userService.resetPassword("test_user", "test@example.com", "new_password");
        verify(userMapper).updateUser(any(User.class));
    }

    @Test
    public void testResetPassword_Fail_InvalidUser() {
        when(userMapper.selectUserByEmail("wrong@example.com")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                userService.resetPassword("test_user", "wrong@example.com", "new_password")
        );
    }

    @Test
    public void testChangeUsername_Success() throws Exception {
        when(userMapper.selectUserByUsername("test_user")).thenReturn(testUser);
        when(userMapper.selectUserByUsername("new_user")).thenReturn(null);

        userService.changeUsername("test_user", "new_user", "password");
        verify(userMapper).updateUser(any(User.class));
    }

    @Test
    public void testChangeUsername_Fail_InvalidPassword() {
        when(userMapper.selectUserByUsername("test_user")).thenReturn(testUser);

        Exception exception = assertThrows(Exception.class, () ->
                userService.changeUsername("test_user", "new_user", "wrong_password")
        );
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    public void testChangeUsername_Fail_UsernameAlreadyExists() {
        when(userMapper.selectUserByUsername("test_user")).thenReturn(testUser);
        when(userMapper.selectUserByUsername("new_user")).thenReturn(new User());

        Exception exception = assertThrows(Exception.class, () ->
                userService.changeUsername("test_user", "new_user", "password")
        );
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    public void testChangeEmail_Success() throws Exception {
        when(userMapper.selectUserByUsername("test_user")).thenReturn(testUser);
        when(userMapper.selectUserByEmail("new@example.com")).thenReturn(null);

        userService.changeEmail("test_user", "new@example.com", "password");
        verify(userMapper).updateUser(any(User.class));
    }

    @Test
    public void testChangeEmail_Fail_InvalidPassword() {
        when(userMapper.selectUserByUsername("test_user")).thenReturn(testUser);

        Exception exception = assertThrows(Exception.class, () ->
                userService.changeEmail("test_user", "new@example.com", "wrong_password")
        );
        assertEquals("Invalid password", exception.getMessage());
    }

    @Test
    public void testChangeEmail_Fail_EmailAlreadyExists() {
        when(userMapper.selectUserByUsername("test_user")).thenReturn(testUser);
        when(userMapper.selectUserByEmail("new@example.com")).thenReturn(new User());

        Exception exception = assertThrows(Exception.class, () ->
                userService.changeEmail("test_user", "new@example.com", "password")
        );
        assertEquals("Email already exists", exception.getMessage());
    }
}
