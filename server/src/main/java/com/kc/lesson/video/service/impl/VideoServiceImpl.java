package com.kc.lesson.video.service.impl;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Video;
import com.kc.lesson.video.entity.VideoTag;
import com.kc.lesson.video.mapper.VideoMapper;
import com.kc.lesson.video.mapper.VideoTagMapper;
import com.kc.lesson.video.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    VideoMapper mapper;

    @Autowired
    VideoTagMapper videoTagMapper;

    @Override
    public PageInfo<Video> getVideoList(String keyword, String sort, String c, String status, String tag, String isAdmin, int page, int pageSize) {
        // 使用 PageHelper 设置分页参数
        PageHelper.startPage(page, pageSize);

        List<Video> videos = mapper.getVideoList(keyword, sort, c, status, tag, isAdmin);

        // tag筛选
        if (StringUtils.isNotBlank(tag)) {
            List<Video> tVideos = new ArrayList<>();
            List<VideoTag> videoTagList = videoTagMapper.selectByTagId(tag);
            for (Video video : videos) {
                for (VideoTag videoTag : videoTagList) {
                    if (video.getId().equals(videoTag.getVideoId())) {
                        tVideos.add(video);
                    }
                }
            }
            videos.clear();
            videos.addAll(tVideos);
        }

        // 附加tag
        for (Video video : videos) {
            List<VideoTag> videoTags = videoTagMapper.selectByVideoId(video.getId());
            List<Long> tags = videoTags.stream().map(VideoTag::getTagId).collect(Collectors.toList());
            video.setTags(tags);
        }

        // 使用 PageInfo 包装结果，并返回
        return new PageInfo<>(videos);
    }

    @Override
    public void createVideo(Video video) {
        video.setCreateTime(String.valueOf(System.currentTimeMillis()));
        if (video.getPv() == null) {
            video.setPv("0");
        }
        if (video.getScore() == null) {
            video.setScore("0");
        }
        if (video.getWishCount() == null) {
            video.setWishCount("0");
        }
        mapper.insert(video);
        setVideoTags(video);
    }

    @Override
    public void deleteVideo(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateVideo(Video video) {
        setVideoTags(video);
        mapper.updateSelective(video);
    }

    @Override
    public void updateVideoPv(Video video){
        mapper.updateSelective(video);
    }

    @Override
    public Video getVideoById(String id) {
        return mapper.selectById(id);
    }

    @Override
    public void addWishCount(String videoId) {
        Video video = mapper.selectById(videoId);
        video.setWishCount(String.valueOf(Integer.parseInt(video.getWishCount()) + 1));
        mapper.updateSelective(video);
    }

    @Override
    public void addCollectCount(String videoId) {
        Video video = mapper.selectById(videoId);
        video.setCollectCount(String.valueOf(Integer.parseInt(video.getCollectCount()) + 1));
        mapper.updateSelective(video);
    }

    public void setVideoTags(Video video) {
        Map<String, Object> map = new HashMap<>();
        map.put("videoId", video.getId());
        videoTagMapper.deleteByMap(map);

        if (video.getTags() != null) {
            for (Long tag : video.getTags()) {
                VideoTag videoTag = new VideoTag();
                videoTag.setVideoId(video.getId());
                videoTag.setTagId(tag);
                videoTagMapper.insert(videoTag);
            }
        }
    }

}
