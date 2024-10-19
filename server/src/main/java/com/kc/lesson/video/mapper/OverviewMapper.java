package com.kc.lesson.video.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kc.lesson.video.entity.VisitData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OverviewMapper extends BaseMapper<Object> {

    List<Object> getPopularVideo();
    List<Object> getPopularClassification();
    List<VisitData> getWebVisitData(@Param("day") String day);

}
