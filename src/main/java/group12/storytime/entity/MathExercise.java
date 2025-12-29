package group12.storytime.entity;

import java.util.UUID;

public class MathExercise {
    private UUID ExerciseID;
    private UUID VideoID;
    private String Question;
    private String Answer;
    private String Feedback; // 可为空

    // Getters and Setters
    public UUID getExerciseID() {
        return ExerciseID;
    }

    public void setExerciseID(UUID exerciseID) {
        ExerciseID = exerciseID;
    }

    public UUID getVideoID() {
        return VideoID;
    }

    public void setVideoID(UUID videoID) {
        VideoID = videoID;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }
}
