package com.storytime.repository;

import com.storytime.entity.StoryImage;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StoryImageMapper {
    @Insert("INSERT INTO \"StoryImage\" (\"ImageID\", \"PrivateFairyTaleID\", \"PublicFairyTaleID\", \"ImageURL\") " +
            "VALUES (#{ImageID}, #{PrivateFairytaleID}, #{PublicFairytaleID}, #{ImageURL})")
    void insertStoryImage(StoryImage image);
}
