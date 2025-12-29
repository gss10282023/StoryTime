package com.storytime.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
@Data

@Entity
@Table(name = "Users") // 确保与数据库中的表名称一致
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userID", updatable = false, nullable = false)
    private UUID userID;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "registration_date")
    private LocalDateTime registrationDate;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    public User() {
    }

    public User(String username, String password, String email) {
        this.userID = UUID.randomUUID();
        this.username = username;
        this.password = password;
        this.email = email;
        this.registrationDate = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
    }
}