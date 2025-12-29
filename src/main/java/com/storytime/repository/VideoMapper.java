package com.storytime.repository;

import com.storytime.entity.Video;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VideoMapper {

    @Insert("INSERT INTO \"Video\" (\"VideoID\", \"UserID\", \"CreationDate\", \"Subject\", \"AgeGroup\", \"MainCharacter\", \"VideoURL\", \"Language\") " +
            "VALUES (#{VideoID}, #{UserID}, #{CreationDate}, #{Subject}, #{AgeGroup}, #{MainCharacter}, #{VideoURL}, #{Language})")
    void insertVideo(Video video);

    @Select("SELECT \"UserID\", \"VideoID\", \"CreationDate\", \"Language\", \"Subject\", \"VideoURL\" FROM \"Video\"")
    List<Video> findAllVideos();
}
