package com.kc.lesson.log.service;


import com.github.pagehelper.PageInfo;
import com.kc.lesson.log.entity.OpLog;

public interface OpLogService {
    PageInfo<OpLog> getOpLogList(int page, int pageSize);

    void createOpLog(OpLog opLog);

    void deleteOpLog(String id);

    void updateOpLog(OpLog opLog);

    PageInfo<OpLog> getLoginLogList(int page, int pageSize);
}
