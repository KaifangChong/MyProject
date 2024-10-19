package com.kc.lesson.video.controller;

import com.github.pagehelper.PageInfo;
import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.permission.RolePermission;
import com.kc.lesson.permission.RolePermissionLevel;
import com.kc.lesson.video.entity.Classification;
import com.kc.lesson.video.service.ClassificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/classification")
public class ClassificationController {

    private final static Logger logger = LoggerFactory.getLogger(ClassificationController.class);

    @Autowired
    ClassificationService service;

    // 查询分类列表
    @GetMapping("/list")
    public KCResponse list(String cName,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "1000") int pageSize) {
        try {
            PageInfo<Classification> list = service.getClassificationList(cName, page, pageSize);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            logger.error("查询分类列表失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    // 添加分类
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/create")
    @Transactional
    public KCResponse create(Classification classification) throws IOException {
        try {
            service.createClassification(classification);
            return new KCResponse(ResponseCode.SUCCESS, "创建成功");
        } catch (Exception e) {
            logger.error("创建分类失败", e);
            return new KCResponse(ResponseCode.FAIL, "创建失败");
        }
    }

    // 删除分类
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/delete")
    @Transactional
    public KCResponse delete(String ids) {
        try {
            String[] arr = ids.split(",");
            for (String id : arr) {
                service.deleteClassification(id);
            }
            return new KCResponse(ResponseCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除分类失败", e);
            return new KCResponse(ResponseCode.FAIL, "删除失败");
        }
    }

    // 更新分类
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/update")
    @Transactional
    public KCResponse update(Classification classification) throws IOException {
        try {
            service.updateClassification(classification);
            return new KCResponse(ResponseCode.SUCCESS, "更新成功");
        } catch (Exception e) {
            logger.error("更新分类失败", e);
            return new KCResponse(ResponseCode.FAIL, "更新失败");
        }
    }

}
