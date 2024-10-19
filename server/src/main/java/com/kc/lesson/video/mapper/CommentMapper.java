package com.kc.lesson.video.mapper;

import com.kc.lesson.video.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    List<Comment> getList(String userName, String videoName);

    List<Comment> selectVideoCommentList(String videoId, String order);

    List<Comment> selectUserCommentList(String userId);

    void insert(Comment comment);

    void deleteById(String id);

    void updateById(Comment comment);

    Comment selectById(String id);
}
