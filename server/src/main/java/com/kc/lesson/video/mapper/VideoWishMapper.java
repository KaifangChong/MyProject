package com.kc.lesson.video.mapper;

import com.kc.lesson.video.entity.VideoWish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoWishMapper {

    List<Map> getVideoWishList(String userId);

    void insert(VideoWish videoWish);

    void deleteById(String id);

    VideoWish getVideoWishByUserAndVideo(@Param("userId") String userId, @Param("videoId") String videoId);

    VideoWish getVideoWishById(String wishId);
}
