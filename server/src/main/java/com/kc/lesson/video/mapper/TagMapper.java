package com.kc.lesson.video.mapper;

import com.kc.lesson.video.entity.Tag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper {

    List<Tag> selectAll(String tagName);

    void insert(Tag tag);

    void deleteById(String id);

    void updateById(Tag tag);
}
