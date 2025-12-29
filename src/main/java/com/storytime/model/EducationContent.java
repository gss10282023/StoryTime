// EducationContent.java
package com.storytime.model;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EducationContent {
    private String subject; // ENUM('Math','English')
    private int ageGroup; // INT
    private String mainCharacter; // VARCHAR(50)
    private String description; // TEXT

    // 数学题特有字段
    private String question; // TEXT
    private String answer; // TEXT
    private String feedback; // TEXT

    // 构造函数、Getter和Setter方法
    // 根据需要添加
}
