package com.kc.lesson.video.controller;

import com.github.pagehelper.PageInfo;
import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.permission.RolePermission;
import com.kc.lesson.permission.RolePermissionLevel;
import com.kc.lesson.video.entity.Comment;
import com.kc.lesson.video.service.CommentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    private final static Logger logger = LoggerFactory.getLogger(CommentController.class);

    @Autowired
    CommentService service;

    // 查询评论列表
    @GetMapping("/list")
    public KCResponse list(String userName, String videoName,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "1000") int pageSize) {
        try {
            PageInfo<Comment> list = service.getCommentList(userName, videoName, page, pageSize);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            logger.error("查询评论列表失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    // 视频的所有评论
    @GetMapping("/listVideoComments")
    public KCResponse listVideoComments(@RequestParam("videoId") String videoId, @RequestParam("order") String order) {
        try {
            List<Comment> list = service.getVideoCommentList(videoId, order);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            logger.error("查询视频评论失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    // 用户的所有评论
    @GetMapping("/listUserComments")
    public KCResponse listUserComments(String userId) {
        try {
            List<Comment> list = service.getUserCommentList(userId);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            logger.error("查询用户评论失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    // 添加评论
    @PostMapping("/create")
    @Transactional
    public KCResponse create(Comment comment) throws IOException {
        try {
            service.createComment(comment);
            return new KCResponse(ResponseCode.SUCCESS, "创建成功");
        } catch (Exception e) {
            logger.error("创建评论失败", e);
            return new KCResponse(ResponseCode.FAIL, "创建失败");
        }
    }

    // 删除评论
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/delete")
    @Transactional
    public KCResponse delete(String ids) {
        try {
            String[] arr = ids.split(",");
            for (String id : arr) {
                service.deleteComment(id);
            }
            return new KCResponse(ResponseCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除评论失败", e);
            return new KCResponse(ResponseCode.FAIL, "删除失败");
        }
    }

    // 更新评论
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/update")
    @Transactional
    public KCResponse update(Comment comment) throws IOException {
        try {
            service.updateComment(comment);
            return new KCResponse(ResponseCode.SUCCESS, "更新成功");
        } catch (Exception e) {
            logger.error("更新评论失败", e);
            return new KCResponse(ResponseCode.FAIL, "更新失败");
        }
    }

    // 评论点赞
    @PostMapping("/like")
    @Transactional
    public KCResponse like(String id) throws IOException {
        try {
            Comment comment = service.getCommentDetail(id);
            int likeCount = Integer.parseInt(comment.getLikeCount()) + 1;
            comment.setLikeCount(String.valueOf(likeCount));
            service.updateComment(comment);
            return new KCResponse(ResponseCode.SUCCESS, "点赞成功");
        } catch (Exception e) {
            logger.error("点赞评论失败", e);
            return new KCResponse(ResponseCode.FAIL, "点赞失败");
        }
    }

}
