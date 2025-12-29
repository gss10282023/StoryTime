package group12.storytime.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EducationContentTest {

    private EducationContent educationContent;

    @BeforeEach
    public void setUp() {
        educationContent = new EducationContent();
    }

    @Test
    public void testSubject() {
        String subject = "Math";
        educationContent.setSubject(subject);
        assertEquals(subject, educationContent.getSubject());
    }

    @Test
    public void testAgeGroup() {
        int ageGroup = 5;
        educationContent.setAgeGroup(ageGroup);
        assertEquals(ageGroup, educationContent.getAgeGroup());
    }

    @Test
    public void testMainCharacter() {
        String mainCharacter = "Curious Cat";
        educationContent.setMainCharacter(mainCharacter);
        assertEquals(mainCharacter, educationContent.getMainCharacter());
    }

    @Test
    public void testDescription() {
        String description = "An educational story about math concepts.";
        educationContent.setDescription(description);
        assertEquals(description, educationContent.getDescription());
    }

    @Test
    public void testQuestion() {
        String question = "What is 5 + 3?";
        educationContent.setQuestion(question);
        assertEquals(question, educationContent.getQuestion());
    }

    @Test
    public void testAnswer() {
        String answer = "8";
        educationContent.setAnswer(answer);
        assertEquals(answer, educationContent.getAnswer());
    }

    @Test
    public void testFeedback() {
        String feedback = "Correct! 5 + 3 is indeed 8.";
        educationContent.setFeedback(feedback);
        assertEquals(feedback, educationContent.getFeedback());
    }

    @Test
    public void testSettersAndGetters() {
        // 设置所有字段的值
        String subject = "English";
        int ageGroup = 10;
        String mainCharacter = "Brave Knight";
        String description = "An adventure story to teach English.";
        String question = "What is the past tense of 'go'?";
        String answer = "Went";
        String feedback = "Correct! The past tense of 'go' is 'went'.";

        // 设置值
        educationContent.setSubject(subject);
        educationContent.setAgeGroup(ageGroup);
        educationContent.setMainCharacter(mainCharacter);
        educationContent.setDescription(description);
        educationContent.setQuestion(question);
        educationContent.setAnswer(answer);
        educationContent.setFeedback(feedback);

        // 验证值
        assertEquals(subject, educationContent.getSubject());
        assertEquals(ageGroup, educationContent.getAgeGroup());
        assertEquals(mainCharacter, educationContent.getMainCharacter());
        assertEquals(description, educationContent.getDescription());
        assertEquals(question, educationContent.getQuestion());
        assertEquals(answer, educationContent.getAnswer());
        assertEquals(feedback, educationContent.getFeedback());
    }
}
