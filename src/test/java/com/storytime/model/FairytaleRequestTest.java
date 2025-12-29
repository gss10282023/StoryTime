package com.storytime.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FairytaleRequestTest {

    private FairytaleRequest request;

    @BeforeEach
    public void setUp() {
        request = new FairytaleRequest();
    }

    @Test
    public void testDefaultConstructor() {
        // 默认构造函数测试
        assertNull(request.getAgeGroup());
        assertNull(request.getGender());
        assertNull(request.getName());
        assertNull(request.getCharacteristic());
        assertFalse(request.isGenerateVideo());
    }

    @Test
    public void testParameterizedConstructor() {
        // 带参数的构造函数测试
        request = new FairytaleRequest("6-8", "Female", "Alice", "Brave", true);

        assertEquals("6-8", request.getAgeGroup());
        assertEquals("Female", request.getGender());
        assertEquals("Alice", request.getName());
        assertEquals("Brave", request.getCharacteristic());
        assertTrue(request.isGenerateVideo());
    }

    @Test
    public void testSettersAndGetters() {
        // 测试 set 和 get 方法
        request.setAgeGroup("9-12");
        assertEquals("9-12", request.getAgeGroup());

        request.setGender("Male");
        assertEquals("Male", request.getGender());

        request.setName("Tom");
        assertEquals("Tom", request.getName());

        request.setCharacteristic("Curious");
        assertEquals("Curious", request.getCharacteristic());

        request.setGenerateVideo(false);
        assertFalse(request.isGenerateVideo());

        request.setGenerateVideo(true);
        assertTrue(request.isGenerateVideo());
    }
}
