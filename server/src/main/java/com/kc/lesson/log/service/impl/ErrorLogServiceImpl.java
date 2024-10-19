package com.kc.lesson.log.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.lesson.log.entity.ErrorLog;
import com.kc.lesson.log.mapper.ErrorLogMapper;
import com.kc.lesson.log.service.ErrorLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ErrorLogServiceImpl implements ErrorLogService {
    @Autowired
    ErrorLogMapper mapper;

    @Override
    public PageInfo<ErrorLog> getErrorLogList(int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        List<ErrorLog> list = mapper.selectAll();
        // 使用 PageInfo 封装分页信息
        return new PageInfo<>(list);
    }

    @Override
    public void createErrorLog(ErrorLog errorLog) {
        mapper.insert(errorLog);
    }

    @Override
    public void deleteErrorLog(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateErrorLog(ErrorLog errorLog) {
        mapper.updateById(errorLog);
    }
}
