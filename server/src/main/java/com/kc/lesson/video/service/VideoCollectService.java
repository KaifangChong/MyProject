package com.kc.lesson.video.service;


import com.kc.lesson.video.entity.VideoCollect;

import java.util.List;
import java.util.Map;

public interface VideoCollectService {
    List<Map> getVideoCollectList(String userId);

    void createVideoCollect(VideoCollect videoCollect);

    void deleteVideoCollect(String id);

    VideoCollect getVideoCollect(String userId, String videoId);

    VideoCollect getVideoCollectById(String collectId);
}
