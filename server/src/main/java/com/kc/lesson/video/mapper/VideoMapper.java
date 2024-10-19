package com.kc.lesson.video.mapper;

import com.kc.lesson.video.entity.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface VideoMapper {
    List<Video> getVideoList(@Param("keyword") String keyword,
                             @Param("sort") String sort,
                             @Param("c") String c,
                             @Param("status") String status,
                             @Param("tag") String tag,
                             @Param("isAdmin") String isAdmin);

    void insert(Video video);

    void deleteById(String id);

    void updateById(Video video);

    Video selectById(String id);

    long countAllVideos();

    long countRecentVideos(long sevenMillis);

    int updateSelective(Video video);
}
