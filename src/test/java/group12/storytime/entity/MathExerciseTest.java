package group12.storytime.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class MathExerciseTest {

    private MathExercise mathExercise;
    private UUID exerciseID;
    private UUID videoID;
    private String question;
    private String answer;
    private String feedback;

    @BeforeEach
    public void setUp() {
        mathExercise = new MathExercise();
        exerciseID = UUID.randomUUID();
        videoID = UUID.randomUUID();
        question = "What is 5 + 3?";
        answer = "8";
        feedback = "Correct! 5 + 3 is 8.";
    }

    @Test
    public void testSetAndGetExerciseID() {
        mathExercise.setExerciseID(exerciseID);
        assertEquals(exerciseID, mathExercise.getExerciseID());
    }

    @Test
    public void testSetAndGetVideoID() {
        mathExercise.setVideoID(videoID);
        assertEquals(videoID, mathExercise.getVideoID());
    }

    @Test
    public void testSetAndGetQuestion() {
        mathExercise.setQuestion(question);
        assertEquals(question, mathExercise.getQuestion());
    }

    @Test
    public void testSetAndGetAnswer() {
        mathExercise.setAnswer(answer);
        assertEquals(answer, mathExercise.getAnswer());
    }

    @Test
    public void testSetAndGetFeedback() {
        mathExercise.setFeedback(feedback);
        assertEquals(feedback, mathExercise.getFeedback());
    }

    @Test
    public void testSetAndGetNullableFeedback() {
        mathExercise.setFeedback(null);
        assertNull(mathExercise.getFeedback());
    }

    @Test
    public void testDefaultValues() {
        MathExercise defaultExercise = new MathExercise();
        assertNull(defaultExercise.getExerciseID());
        assertNull(defaultExercise.getVideoID());
        assertNull(defaultExercise.getQuestion());
        assertNull(defaultExercise.getAnswer());
        assertNull(defaultExercise.getFeedback());
    }
}
