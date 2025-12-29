package group12.storytime.service;


import group12.storytime.entity.User;
import group12.storytime.repository.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService implements UserDetailsService {
    public static final Logger log = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserMapper userMapper;

    // Introduce BCryptPasswordEncoder to hash passwords
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User login(String usernameOrEmail, String password) {
        User user = null;
        if (usernameOrEmail.contains("@")) {
            user = userMapper.selectUserByEmail(usernameOrEmail);
        } else {
            user = userMapper.selectUserByUsername(usernameOrEmail);
        }
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            LocalDateTime now = LocalDateTime.now();
            user.setLastLogin(now);
            userMapper.updateLastLogin(user.getUserID(), now);
            return user;
        } else {
            return null;
        }
    }

    public void resetPassword(String username, String email, String newPassword) throws Exception {
        User user = userMapper.selectUserByEmail(email);
        if (user == null || !user.getUsername().equals(username)) {
            throw new IllegalArgumentException("Invalid username or email.");
        }
        // If user found, update password and encode it
        user.setPassword(passwordEncoder.encode(newPassword));
        userMapper.updateUser(user);
    }

    //    public void register(User user) throws Exception {
//        if (userMapper.selectUserByEmail(user.getEmail()) != null) {
//            throw new Exception("Email already registered");
//        }
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        userMapper.insertUser(user);
//        log.info("User registered successfully: {}", user.getUsername());
//    }
    public void register(User user) throws Exception {
        if (userMapper.selectUserByEmail(user.getEmail()) != null) {
            throw new Exception("Email already registered");
        }

        log.info("User password before encoding: {}", user.getPassword());


        user.setPassword(passwordEncoder.encode(user.getPassword()));


        log.info("User password after encoding: {}", user.getPassword());

        userMapper.insertUser(user);

        log.info("User registered successfully in database: {}", user.getUsername());
    }


//    public void changeUsername(String email, String password, String newUsername) throws Exception {
//        User user = userMapper.selectUserByEmail(email);
//        if (user == null || !user.getPassword().equals(password)) {
//            throw new IllegalArgumentException("Invalid email or password.");
//        }
//        // If user found and password matches, update username
//        user.setUsername(newUsername);
//        userMapper.updateUser(user);
//    }
//
//    public void changeEmail(String username, String password, String newEmail) throws Exception {
//        User user = userMapper.selectUserByUsername(username);
//        if (user == null || !user.getPassword().equals(password)) {
//            throw new IllegalArgumentException("Invalid username or password.");
//        }
//        // If user found and password matches, update email
//        user.setEmail(newEmail);
//        userMapper.updateUser(user);
//        User updatedUser = userMapper.selectUserByUsername(username);
//
//        // Print user information to the command line
//        System.out.println("User information after email change: " + updatedUser);
//    }

    public void changeUsername(String currentUsername, String newUsername, String password) throws Exception {
        User user = userMapper.selectUserByUsername(currentUsername);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid password");
        }
        if (userMapper.selectUserByUsername(newUsername) != null) {
            throw new Exception("Username already exists");
        }
        user.setUsername(newUsername);
        userMapper.updateUser(user);
    }

    public void changeEmail(String username, String newEmail, String password) throws Exception {
        User user = userMapper.selectUserByUsername(username);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new Exception("Invalid password");
        }
        if (userMapper.selectUserByEmail(newEmail) != null) {
            throw new Exception("Email already exists");
        }
        user.setEmail(newEmail);
        userMapper.updateUser(user);
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = null;
        if (usernameOrEmail.contains("@")) {
            user = userMapper.selectUserByEmail(usernameOrEmail);
        } else {
            user = userMapper.selectUserByUsername(usernameOrEmail);
        }
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
        }
        // 返回一个 UserDetails 对象
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER") // 您可以根据需要设置用户的角色
                .build();
    }
}