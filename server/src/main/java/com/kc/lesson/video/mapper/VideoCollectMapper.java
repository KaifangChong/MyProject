package com.kc.lesson.video.mapper;

import com.kc.lesson.video.entity.VideoCollect;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoCollectMapper {
    List<Map> getVideoCollectList(String userId);

    void insert(VideoCollect videoCollect);

    void deleteById(String id);

    VideoCollect getVideoCollectByUserAndVideo(@Param("userId") String userId, @Param("videoId") String videoId);

    VideoCollect getVideoCollectById(String collectId);
}
