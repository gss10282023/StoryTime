package group12.storytime;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class StoryTimeApplicationTests {

    @Test
    void contextLoads() {
    }
    @Test
    void testMainMethod() {
        // 调用 main 方法
        StoryTimeApplication.main(new String[] {});
    }

}
