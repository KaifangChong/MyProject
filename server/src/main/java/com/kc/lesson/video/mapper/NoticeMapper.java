package com.kc.lesson.video.mapper;

import com.kc.lesson.video.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    List<Notice> selectAll(String keyword);

    void insert(Notice notice);

    void deleteById(String id);

    void updateById(Notice notice);
}
