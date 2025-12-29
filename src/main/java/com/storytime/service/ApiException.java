// ApiException.java
package com.storytime.service;

import org.springframework.http.HttpStatus;

/**
 * ApiException 类用于处理 API 调用失败时的异常。
 */
public class ApiException extends Exception {
    private HttpStatus statusCode; // HTTP 状态码

    public ApiException(HttpStatus statusCode) {
        super("API request failed with status code: " + statusCode);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
