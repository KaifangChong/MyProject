package com.kc.lesson.log.controller;

import com.github.pagehelper.PageInfo;
import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.log.entity.OpLog;
import com.kc.lesson.log.service.OpLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

// 负责操作日志和登录日志
@RestController
@RequestMapping("/opLog")
public class OpLogController {

    private final static Logger logger = LoggerFactory.getLogger(OpLogController.class);

    @Autowired
    OpLogService service;

    // 查询日志列表
    @GetMapping("/list")
    public KCResponse list(@RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "1000") int pageSize) {
        try {
            PageInfo<OpLog> list = service.getOpLogList(page,pageSize);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            logger.error("查询日志列表失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    // 查询登录日志列表
    @GetMapping("/loginLogList")
    public KCResponse loginLogList(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "1000") int pageSize) {
        try {
            PageInfo<OpLog> list = service.getLoginLogList(page,pageSize);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            logger.error("查询登录日志列表失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    // 添加日志
    @PostMapping("/create")
    @Transactional
    public KCResponse create(OpLog opLog) throws IOException {
        try {
            service.createOpLog(opLog);
            return new KCResponse(ResponseCode.SUCCESS, "创建成功");
        } catch (Exception e) {
            logger.error("创建日志失败", e);
            return new KCResponse(ResponseCode.FAIL, "创建失败");
        }
    }

    // 删除日志
    @PostMapping("/delete")
    @Transactional
    public KCResponse delete(String ids) {
        try {
            String[] arr = ids.split(",");
            for (String id : arr) {
                service.deleteOpLog(id);
            }
            return new KCResponse(ResponseCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除日志失败", e);
            return new KCResponse(ResponseCode.FAIL, "删除失败");
        }
    }

    // 更新日志
    @PostMapping("/update")
    @Transactional
    public KCResponse update(OpLog opLog) throws IOException {
        try {
            service.updateOpLog(opLog);
            return new KCResponse(ResponseCode.SUCCESS, "更新成功");
        } catch (Exception e) {
            logger.error("更新日志失败", e);
            return new KCResponse(ResponseCode.FAIL, "更新失败");
        }
    }

}
