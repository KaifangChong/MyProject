package com.kc.lesson.video.controller;

import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.permission.RolePermission;
import com.kc.lesson.permission.RolePermissionLevel;
import com.kc.lesson.video.entity.Video;
import com.kc.lesson.video.entity.VideoCollect;
import com.kc.lesson.video.service.VideoCollectService;
import com.kc.lesson.video.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/videoCollect")
public class VideoCollectController {

    private final static Logger logger = LoggerFactory.getLogger(VideoCollectController.class);

    @Autowired
    VideoCollectService videoCollectService;

    @Autowired
    VideoService videoService;

    // 添加收藏
    @RolePermission(level = RolePermissionLevel.LOGIN)
    @PostMapping("/collect")
    @Transactional
    public KCResponse collect(VideoCollect videoCollect) throws IOException {
        try {
            if (videoCollectService.getVideoCollect(videoCollect.getUserId(), videoCollect.getVideoId()) != null) {
                return new KCResponse(ResponseCode.SUCCESS, "您已收藏过了");
            }
            videoCollectService.createVideoCollect(videoCollect);
            videoService.addCollectCount(videoCollect.getVideoId());
            return new KCResponse(ResponseCode.SUCCESS, "收藏成功");
        } catch (Exception e) {
            logger.error("收藏失败", e);
            return new KCResponse(ResponseCode.FAIL, "收藏失败");
        }
    }

    // 取消收藏
    @RolePermission(level = RolePermissionLevel.LOGIN)
    @PostMapping("/unCollect")
    @Transactional
    public KCResponse unCollect(String id) throws IOException {
        try {
            VideoCollect collect = videoCollectService.getVideoCollectById(id);
            if (collect == null) {
                return new KCResponse(ResponseCode.FAIL, "未找到该收藏记录");
            }

            Video video = videoService.getVideoById(collect.getVideoId());
            video.setCollectCount(String.valueOf(Integer.parseInt(video.getCollectCount()) - 1));
            videoService.updateVideo(video);
            videoCollectService.deleteVideoCollect(id);

            return new KCResponse(ResponseCode.SUCCESS, "取消收藏成功");
        } catch (Exception e) {
            logger.error("取消收藏失败", e);
            return new KCResponse(ResponseCode.FAIL, "取消收藏失败");
        }
    }

    // 获取用户收藏列表
    @GetMapping("/getUserCollectList")
    @Transactional
    public KCResponse getUserCollectList(String userId) throws IOException {
        try {
            List<Map> collectList = videoCollectService.getVideoCollectList(userId);
            return new KCResponse(ResponseCode.SUCCESS, "获取成功", collectList);
        } catch (Exception e) {
            logger.error("获取用户收藏列表失败", e);
            return new KCResponse(ResponseCode.FAIL, "获取收藏列表失败");
        }
    }
}
