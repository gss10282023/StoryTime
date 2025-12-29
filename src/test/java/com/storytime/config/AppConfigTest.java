package com.storytime.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

public class AppConfigTest {

    @Test
    public void testRestTemplateBeanCreation() {
        // 创建应用上下文并注册 AppConfig 类
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        // 检查是否成功创建了 RestTemplate bean
        RestTemplate restTemplate = context.getBean(RestTemplate.class);
        assertNotNull(restTemplate, "RestTemplate bean should not be null");

        // 关闭上下文
        context.close();
    }
}
