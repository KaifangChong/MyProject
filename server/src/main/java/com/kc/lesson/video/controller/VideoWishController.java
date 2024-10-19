package com.kc.lesson.video.controller;

import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.permission.RolePermission;
import com.kc.lesson.permission.RolePermissionLevel;
import com.kc.lesson.video.entity.Video;
import com.kc.lesson.video.entity.VideoWish;
import com.kc.lesson.video.service.VideoService;
import com.kc.lesson.video.service.VideoWishService;
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
@RequestMapping("/videoWish")
public class VideoWishController {

    private final static Logger logger = LoggerFactory.getLogger(VideoWishController.class);

    @Autowired
    VideoWishService videoWishService;

    @Autowired
    VideoService videoService;

    // 添加喜欢
    @RolePermission(level = RolePermissionLevel.LOGIN)
    @PostMapping("/wish")
    @Transactional
    public KCResponse wish(VideoWish videoWish) throws IOException {
        try {
            if (videoWishService.getVideoWish(videoWish.getUserId(), videoWish.getVideoId()) != null) {
                return new KCResponse(ResponseCode.SUCCESS, "您已添加过了");
            }
            videoWishService.createVideoWish(videoWish);
            videoService.addWishCount(videoWish.getVideoId());
            return new KCResponse(ResponseCode.SUCCESS, "添加成功");
        } catch (Exception e) {
            logger.error("添加喜欢失败", e);
            return new KCResponse(ResponseCode.FAIL, "添加失败");
        }
    }

    // 取消喜欢
    @RolePermission(level = RolePermissionLevel.LOGIN)
    @PostMapping("/unWish")
    @Transactional
    public KCResponse unWish(String id) throws IOException {
        try {
            VideoWish wish = videoWishService.getVideoWishById(id);
            if (wish == null) {
                return new KCResponse(ResponseCode.FAIL, "喜欢记录未找到");
            }

            Video video = videoService.getVideoById(wish.getVideoId());
            video.setWishCount(String.valueOf(Integer.parseInt(video.getWishCount()) - 1));
            videoService.updateVideo(video);
            videoWishService.deleteVideoWish(id);

            return new KCResponse(ResponseCode.SUCCESS, "取消喜欢成功");
        } catch (Exception e) {
            logger.error("取消喜欢失败", e);
            return new KCResponse(ResponseCode.FAIL, "取消喜欢失败");
        }
    }

    // 获取用户的喜欢列表
    @GetMapping("/getUserWishList")
    @Transactional
    public KCResponse getUserWishList(String userId) throws IOException {
        try {
            List<Map> wishList = videoWishService.getVideoWishList(userId);
            return new KCResponse(ResponseCode.SUCCESS, "获取成功", wishList);
        } catch (Exception e) {
            logger.error("获取用户喜欢列表失败", e);
            return new KCResponse(ResponseCode.FAIL, "获取失败");
        }
    }
}
