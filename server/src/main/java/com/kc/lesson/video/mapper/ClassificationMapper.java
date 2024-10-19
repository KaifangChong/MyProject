package com.kc.lesson.video.mapper;

import com.kc.lesson.video.entity.Classification;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassificationMapper {

    List<Classification> selectAll(String cName);

    void insert(Classification classification);

    void deleteById(String id);

    void updateById(Classification classification);
}
