package com.storytime.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.services.s3.S3Client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class S3ConfigTest {

    @InjectMocks
    private S3Config s3Config;

    @Mock
    private S3Client mockS3Client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // 手动注入模拟的配置属性值
        setField(s3Config, "accessKey", "mockAccessKey");
        setField(s3Config, "secretKey", "mockSecretKey");
        setField(s3Config, "bucketName", "mockBucketName");
        setField(s3Config, "endpoint", "http://mock-endpoint.com");
    }

    @Test
    void testS3ClientCreation() {
        // 调用 s3Client 方法并验证其不为 null
        S3Client s3Client = s3Config.s3Client();
        assertNotNull(s3Client);
    }

    @Test
    void testGetBucketName() {
        // 验证 bucketName 的返回值
        assertEquals("mockBucketName", s3Config.getBucketName());
    }

    // 辅助方法：用来注入私有字段
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
