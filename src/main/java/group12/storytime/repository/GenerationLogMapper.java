package group12.storytime.repository;


import group12.storytime.entity.GenerationLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GenerationLogMapper {
    @Insert("INSERT INTO \"GenerationLog\" (\"LogID\", \"UserID\", \"VideoID\", \"Timestamp\") " +
            "VALUES (#{LogID}, #{UserID}, #{VideoID}, #{Timestamp})")
    void insertGenerationLog(GenerationLog log);
}
