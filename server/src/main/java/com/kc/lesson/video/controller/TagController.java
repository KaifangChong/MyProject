package com.kc.lesson.video.controller;

import com.github.pagehelper.PageInfo;
import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.permission.RolePermission;
import com.kc.lesson.permission.RolePermissionLevel;
import com.kc.lesson.video.entity.Tag;
import com.kc.lesson.video.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping("/tag")
public class TagController {

    private final static Logger logger = LoggerFactory.getLogger(TagController.class);

    @Autowired
    TagService service;

    // 查询标签列表
    @GetMapping("/list")
    public KCResponse list(String tagName,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "1000") int pageSize) {
        try {
            PageInfo<Tag> list = service.getTagList(tagName, page, pageSize);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
        } catch (Exception e) {
            logger.error("查询标签列表失败", e);
            return new KCResponse(ResponseCode.FAIL, "查询失败");
        }
    }

    // 添加标签
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/create")
    @Transactional
    public KCResponse create(Tag tag) {
        try {
            service.createTag(tag);
            return new KCResponse(ResponseCode.SUCCESS, "创建成功");
        } catch (Exception e) {
            logger.error("创建标签失败", e);
            return new KCResponse(ResponseCode.FAIL, "创建失败");
        }
    }

    // 删除标签
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/delete")
    @Transactional
    public KCResponse delete(String ids) {
        try {
            String[] idArray = ids.split(",");
            Arrays.stream(idArray).forEach(service::deleteTag);
            return new KCResponse(ResponseCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除标签失败", e);
            return new KCResponse(ResponseCode.FAIL, "删除失败");
        }
    }

    // 更新标签
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/update")
    @Transactional
    public KCResponse update(Tag tag) {
        try {
            service.updateTag(tag);
            return new KCResponse(ResponseCode.SUCCESS, "更新成功");
        } catch (Exception e) {
            logger.error("更新标签失败", e);
            return new KCResponse(ResponseCode.FAIL, "更新失败");
        }
    }

}
