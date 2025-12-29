package com.storytime.repository;

import com.storytime.entity.User;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.UUID;

public interface UserMapper {

    @Insert("INSERT INTO \"Users\" (\"UserID\", \"Username\", \"Password\", \"Email\", \"RegistrationDate\", \"LastLogin\") " +
            "VALUES (#{userID, typeHandler=com.storytime.typehandler.UUIDTypeHandler}, #{username}, #{password}, #{email}, #{registrationDate}, #{lastLogin})")
    void insertUser(User user);

    @Select("SELECT * FROM \"Users\" WHERE \"UserID\" = #{userID, typeHandler=com.storytime.typehandler.UUIDTypeHandler}")
    User selectUserById(UUID userID);

    @Select("SELECT * FROM \"Users\" WHERE \"Username\" = #{username}")
    User selectUserByUsername(@Param("username") String username);

    @Select("SELECT * FROM \"Users\" WHERE \"Email\" = #{email}")
    User selectUserByEmail(@Param("email") String email);

    @Update("UPDATE \"Users\" SET \"Username\" = #{username}, \"Password\" = #{password}, \"Email\" = #{email}, \"LastLogin\" = #{lastLogin} " +
            "WHERE \"UserID\" = #{userID, typeHandler=com.storytime.typehandler.UUIDTypeHandler}")
    void updateUser(User user);

    @Delete("DELETE FROM \"Users\" WHERE \"UserID\" = #{userID, typeHandler=com.storytime.typehandler.UUIDTypeHandler}")
    void deleteUser(UUID userID);

    @Update("UPDATE \"Users\" SET \"LastLogin\" = #{lastLogin} WHERE \"UserID\" = #{userID, typeHandler=com.storytime.typehandler.UUIDTypeHandler}")
    void updateLastLogin(@Param("userID") UUID userID, @Param("lastLogin") LocalDateTime lastLogin);
}