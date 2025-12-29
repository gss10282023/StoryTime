package group12.storytime.service;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ApiExceptionTest 类用于测试 ApiException 的行为。
 */
public class ApiExceptionTest {

    @Test
    public void testApiExceptionWithStatusCode() {
        // 创建 ApiException 实例，传入 HttpStatus
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiException exception = new ApiException(status);

        // 验证异常消息是否正确
        String expectedMessage = "API request failed with status code: " + status;
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match");

        // 验证状态码是否正确
        assertEquals(status, exception.getStatusCode(), "Status code should match");
    }

    @Test
    public void testApiExceptionWithDifferentStatusCode() {
        // 使用不同的 HttpStatus 创建 ApiException 实例
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ApiException exception = new ApiException(status);

        // 验证异常消息是否正确
        String expectedMessage = "API request failed with status code: " + status;
        assertEquals(expectedMessage, exception.getMessage(), "Exception message should match");

        // 验证状态码是否正确
        assertEquals(status, exception.getStatusCode(), "Status code should match");
    }
}
