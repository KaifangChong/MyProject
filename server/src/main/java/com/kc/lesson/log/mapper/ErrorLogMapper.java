package com.kc.lesson.log.mapper;

import com.kc.lesson.log.entity.ErrorLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ErrorLogMapper {
    List<ErrorLog> selectAll();

    void insert(ErrorLog errorLog);

    void deleteById(String id);

    void updateById(ErrorLog errorLog);
}
