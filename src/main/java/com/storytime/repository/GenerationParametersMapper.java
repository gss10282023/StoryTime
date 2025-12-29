package com.storytime.repository;


import com.storytime.entity.GenerationParameters;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GenerationParametersMapper {
    @Insert("INSERT INTO \"GenerationParameters\" (\"ParameterID\", \"VideoID\", \"Subject\", \"AgeGroup\", \"MainCharacter\", \"Language\", \"ExerciseID\") " +
            "VALUES (#{ParameterID}, #{VideoID}, #{Subject}, #{AgeGroup}, #{MainCharacter}, #{Language}, #{ExerciseID})")
    void insertGenerationParameters(GenerationParameters parameters);
}
