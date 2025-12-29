package com.storytime;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@MapperScan("com.storytime.repository")

@SpringBootApplication


@EnableAsync
public class StoryTimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoryTimeApplication.class, args);
    }

}
