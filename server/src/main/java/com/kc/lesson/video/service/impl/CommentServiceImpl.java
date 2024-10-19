package com.kc.lesson.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Comment;
import com.kc.lesson.video.mapper.CommentMapper;
import com.kc.lesson.video.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentMapper mapper;

    @Override
    public PageInfo<Comment> getCommentList(String userName, String videoName, int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        List<Comment> list = mapper.getList(userName, videoName);
        // 使用 PageInfo 封装分页信息
        return new PageInfo<>(list);
    }

    @Override
    public void createComment(Comment comment) {
        System.out.println(comment);
        comment.setLikeCount("0");
        comment.setCommentTime(String.valueOf(System.currentTimeMillis()));
        mapper.insert(comment);
    }

    @Override
    public void deleteComment(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateComment(Comment comment) {
        mapper.updateById(comment);
    }

    @Override
    public Comment getCommentDetail(String id) {
        return mapper.selectById(id);
    }

    @Override
    public List<Comment> getVideoCommentList(String videoId, String order) {
        return mapper.selectVideoCommentList(videoId, order);
    }

    @Override
    public List<Comment> getUserCommentList(String userId) {
        return mapper.selectUserCommentList(userId);
    }
}
