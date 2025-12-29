package group12.storytime.repository;

import group12.storytime.entity.MathExercise;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MathExerciseMapper {
    @Insert("INSERT INTO \"MathExercise\" (\"ExerciseID\", \"VideoID\", \"Question\", \"Answer\", \"Feedback\") " +
            "VALUES (#{ExerciseID}, #{VideoID}, #{Question}, #{Answer}, #{Feedback})")
    void insertMathExercise(MathExercise exercise);
}
