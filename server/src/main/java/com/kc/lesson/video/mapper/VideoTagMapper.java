package com.kc.lesson.video.mapper;

import com.kc.lesson.video.entity.VideoTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoTagMapper {

    List<VideoTag> selectByTagId(String videoId);

    List<VideoTag> selectByVideoId(Long videoId);

    void insert(VideoTag videoTag);

    void deleteByMap(Map<String, Object> map);
}
