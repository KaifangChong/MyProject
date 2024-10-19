package com.kc.lesson.video.service.impl;

import com.kc.lesson.video.entity.VideoCollect;
import com.kc.lesson.video.mapper.VideoCollectMapper;
import com.kc.lesson.video.service.VideoCollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
class VideoCollectServiceImpl implements VideoCollectService {
    @Autowired
    VideoCollectMapper mapper;

    @Override
    public List<Map> getVideoCollectList(String userId) {
        return mapper.getVideoCollectList(userId);
    }

    @Override
    public void createVideoCollect(VideoCollect videoCollect) {
        mapper.insert(videoCollect);
    }

    @Override
    public void deleteVideoCollect(String id) {
        mapper.deleteById(id);
    }

    @Override
    public VideoCollect getVideoCollect(String userId, String videoId) {
        return mapper.getVideoCollectByUserAndVideo(userId, videoId);
    }

    public VideoCollect getVideoCollectById(String collectId) {
        return mapper.getVideoCollectById(collectId);
    }
}
