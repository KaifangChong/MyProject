package com.kc.lesson.video.controller;

import com.github.pagehelper.PageInfo;
import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.permission.RolePermission;
import com.kc.lesson.permission.RolePermissionLevel;
import com.kc.lesson.video.entity.Notice;
import com.kc.lesson.video.service.NoticeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    private final static Logger logger = LoggerFactory.getLogger(NoticeController.class);

    @Autowired
    NoticeService service;

    // 查询通知列表
    @GetMapping("/list")
    public KCResponse list(String keyword,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "1000") int pageSize) {
        try {
            PageInfo<Notice> list = service.getNoticeList(keyword, page, pageSize);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            logger.error("查询通知列表失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    // 创建通知
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/create")
    @Transactional
    public KCResponse create(Notice notice) throws IOException {
        try {
            service.createNotice(notice);
            return new KCResponse(ResponseCode.SUCCESS, "创建成功");
        } catch (Exception e) {
            logger.error("创建通知失败", e);
            return new KCResponse(ResponseCode.FAIL, "创建失败");
        }
    }

    // 删除通知
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/delete")
    @Transactional
    public KCResponse delete(String ids) {
        try {
            String[] arr = ids.split(",");
            for (String id : arr) {
                service.deleteNotice(id);
            }
            return new KCResponse(ResponseCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除通知失败", e);
            return new KCResponse(ResponseCode.FAIL, "删除失败");
        }
    }

    // 更新通知
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/update")
    @Transactional
    public KCResponse update(Notice notice) throws IOException {
        try {
            service.updateNotice(notice);
            return new KCResponse(ResponseCode.SUCCESS, "更新成功");
        } catch (Exception e) {
            logger.error("更新通知失败", e);
            return new KCResponse(ResponseCode.FAIL, "更新失败");
        }
    }

}
