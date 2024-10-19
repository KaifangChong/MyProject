package com.kc.lesson.video.service.impl;

import com.kc.lesson.video.entity.VideoWish;
import com.kc.lesson.video.mapper.VideoWishMapper;
import com.kc.lesson.video.service.VideoWishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
class VideoWishServiceImpl implements VideoWishService {
    @Autowired
    VideoWishMapper mapper;

    @Override
    public List<Map> getVideoWishList(String userId) {
        return mapper.getVideoWishList(userId);
    }

    @Override
    public void createVideoWish(VideoWish videoWish) {
        mapper.insert(videoWish);
    }

    @Override
    public void deleteVideoWish(String id) {
        mapper.deleteById(id);
    }

    @Override
    public VideoWish getVideoWish(String userId, String videoId) {
        return mapper.getVideoWishByUserAndVideo(userId, videoId);
    }

    public VideoWish getVideoWishById(String wishId) {
        return mapper.getVideoWishById(wishId);
    }
}
