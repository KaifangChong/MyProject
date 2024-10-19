package com.kc.lesson.video.service;


import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Comment;

import java.util.List;

public interface CommentService {
    PageInfo<Comment> getCommentList(String userName, String videoName, int page, int pageSize);

    void createComment(Comment comment);

    void deleteComment(String id);

    void updateComment(Comment comment);

    Comment getCommentDetail(String id);

    List<Comment> getVideoCommentList(String videoId, String order);

    List<Comment> getUserCommentList(String userId);
}
