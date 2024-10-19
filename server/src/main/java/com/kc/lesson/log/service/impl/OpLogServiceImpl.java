package com.kc.lesson.log.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.lesson.log.entity.OpLog;
import com.kc.lesson.log.mapper.OpLogMapper;
import com.kc.lesson.log.service.OpLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpLogServiceImpl implements OpLogService {
    @Autowired
    OpLogMapper mapper;

    @Override
    public PageInfo<OpLog> getOpLogList(int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        List<OpLog> list = mapper.selectOpLogList();
        // 使用 PageInfo 封装分页信息
        return new PageInfo<>(list);
    }

    @Override
    public void createOpLog(OpLog opLog) {
        mapper.insert(opLog);
    }

    @Override
    public void deleteOpLog(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateOpLog(OpLog opLog) {
        mapper.updateById(opLog);
    }

    @Override
    public PageInfo<OpLog> getLoginLogList(int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        List<OpLog> list = mapper.selectLoginLogList();
        // 使用 PageInfo 封装分页信息
        return new PageInfo<>(list);
    }
}
