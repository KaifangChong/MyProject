package com.kc.lesson.log.mapper;

import com.kc.lesson.log.entity.OpLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OpLogMapper {

    List<OpLog> selectOpLogList();

    void insert(OpLog opLog);

    void deleteById(String id);

    void updateById(OpLog opLog);

    List<OpLog> selectLoginLogList();

}
