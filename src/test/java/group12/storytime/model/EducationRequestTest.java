package group12.storytime.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EducationRequestTest {

    private EducationRequest request;

    @BeforeEach
    public void setUp() {
        request = new EducationRequest();
    }

    @Test
    public void testDefaultConstructor() {
        // 默认构造函数测试
        assertNull(request.getMainCharacter());
        assertNull(request.getAgeGroup());
        assertNull(request.getSubject());
        assertNull(request.getUserInput());
        assertTrue(request.isGenerateVideo()); // isGenerateVideo() 总是返回 true
    }

    @Test
    public void testParameterizedConstructor() {
        // 带参数的构造函数测试
        request = new EducationRequest("Rabbit", "6-8", "Math", "Addition", true);

        assertEquals("Rabbit", request.getMainCharacter());
        assertEquals("6-8", request.getAgeGroup());
        assertEquals("Math", request.getSubject());
        assertEquals("Addition", request.getUserInput());
        assertTrue(request.isGenerateVideo()); // isGenerateVideo() 总是返回 true
    }

    @Test
    public void testSettersAndGetters() {
        // 测试 set 和 get 方法
        request.setMainCharacter("Fox");
        assertEquals("Fox", request.getMainCharacter());

        request.setAgeGroup("9-12");
        assertEquals("9-12", request.getAgeGroup());

        request.setSubject("English");
        assertEquals("English", request.getSubject());

        request.setUserInput("Reading");
        assertEquals("Reading", request.getUserInput());

        request.setGenerateVideo(false);
        assertTrue(request.isGenerateVideo()); // 即使设置为 false，isGenerateVideo() 仍应返回 true
    }
}
