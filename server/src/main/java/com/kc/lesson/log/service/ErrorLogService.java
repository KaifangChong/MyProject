package com.kc.lesson.log.service;


import com.github.pagehelper.PageInfo;
import com.kc.lesson.log.entity.ErrorLog;

import java.util.List;

public interface ErrorLogService {
    PageInfo<ErrorLog> getErrorLogList(int page, int pageSize);

    void createErrorLog(ErrorLog errorLog);

    void deleteErrorLog(String id);

    void updateErrorLog(ErrorLog errorLog);
}
