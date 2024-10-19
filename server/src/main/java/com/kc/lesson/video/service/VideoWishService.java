package com.kc.lesson.video.service;


import com.kc.lesson.video.entity.VideoWish;

import java.util.List;
import java.util.Map;

public interface VideoWishService {
    List<Map> getVideoWishList(String userId);

    void createVideoWish(VideoWish videoWish);

    void deleteVideoWish(String id);

    VideoWish getVideoWish(String userId, String videoId);

    VideoWish getVideoWishById(String wishId);
}
