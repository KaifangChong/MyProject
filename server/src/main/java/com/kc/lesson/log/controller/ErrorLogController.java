package com.kc.lesson.log.controller;

import com.github.pagehelper.PageInfo;
import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.log.entity.ErrorLog;
import com.kc.lesson.log.service.ErrorLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/errorLog")
public class ErrorLogController {

    private final static Logger logger = LoggerFactory.getLogger(ErrorLogController.class);

    @Autowired
    ErrorLogService service;

    // 查询错误日志列表
    @GetMapping("/list")
    public KCResponse list(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "1000") int pageSize) {
        try {
            PageInfo<ErrorLog> list = service.getErrorLogList(page, pageSize);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            logger.error("查询错误日志列表失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    // 新增错误日志
    @PostMapping("/create")
    @Transactional
    public KCResponse create(ErrorLog errorLog) throws IOException {
        try {
            service.createErrorLog(errorLog);
            return new KCResponse(ResponseCode.SUCCESS, "创建成功");
        } catch (Exception e) {
            logger.error("创建错误日志失败", e);
            return new KCResponse(ResponseCode.FAIL, "创建失败");
        }
    }

    // 删除错误日志
    @PostMapping("/delete")
    @Transactional
    public KCResponse delete(String ids) {
        try {
            String[] arr = ids.split(",");
            for (String id : arr) {
                service.deleteErrorLog(id);
            }
            return new KCResponse(ResponseCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除错误日志失败", e);
            return new KCResponse(ResponseCode.FAIL, "删除失败");
        }
    }

    // 更新错误日志
    @PostMapping("/update")
    @Transactional
    public KCResponse update(ErrorLog errorLog) throws IOException {
        try {
            service.updateErrorLog(errorLog);
            return new KCResponse(ResponseCode.SUCCESS, "更新成功");
        } catch (Exception e) {
            logger.error("更新错误日志失败", e);
            return new KCResponse(ResponseCode.FAIL, "更新失败");
        }
    }

}
