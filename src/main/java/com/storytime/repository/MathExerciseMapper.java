package com.storytime.repository;

import com.storytime.entity.MathExercise;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MathExerciseMapper {
    @Insert("INSERT INTO \"MathExercise\" (\"ExerciseID\", \"VideoID\", \"Question\", \"Answer\", \"Feedback\") " +
            "VALUES (#{ExerciseID}, #{VideoID}, #{Question}, #{Answer}, #{Feedback})")
    void insertMathExercise(MathExercise exercise);
}
