package com.storytime.repository;


import com.storytime.entity.PrivateFairyTale;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PrivateFairyTaleMapper {
    @Insert("INSERT INTO \"PrivateFairyTale\" (\"PrivateFairyTaleID\", \"UserID\", \"Title\", \"Content\", \"Language\", \"AudioURL\") " +
            "VALUES (#{PrivateFairyTaleID}, #{UserID}, #{Title}, #{Content}, #{Language}, #{AudioURL})")
    void insertPrivateFairyTale(PrivateFairyTale fairyTale);
}
