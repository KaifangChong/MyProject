package com.kc.lesson.video.service;


import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Video;

public interface VideoService {
    PageInfo<Video> getVideoList(String keyword, String sort, String c, String status, String tag, String isAdmin, int page, int pageSize);

    void createVideo(Video video);

    void deleteVideo(String id);

    void updateVideo(Video video);

    void updateVideoPv(Video video);

    Video getVideoById(String id);

    void addWishCount(String videoId);

    void addCollectCount(String videoId);
}
