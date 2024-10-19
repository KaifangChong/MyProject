package com.kc.lesson.video.controller;

import com.github.pagehelper.PageInfo;
import com.kc.lesson.common.KCResponse;
import com.kc.lesson.common.ResponseCode;
import com.kc.lesson.permission.RolePermission;
import com.kc.lesson.permission.RolePermissionLevel;
import com.kc.lesson.video.entity.Video;
import com.kc.lesson.video.service.VideoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/video")
public class VideoController {

    private final static Logger logger = LoggerFactory.getLogger(VideoController.class);

    @Autowired
    VideoService service;

    @Value("${File.uploadPath}")
    private String uploadPath;

    // 获取视频列表
    @GetMapping("/list")
    public KCResponse list(String keyword,
                           String sort,
                           String c,
                           String status,
                           String tag,
                           String isAdmin,
                           @RequestParam(defaultValue = "1") int page,
                           @RequestParam(defaultValue = "10") int pageSize) {
        PageInfo<Video> list = service.getVideoList(keyword, sort, c, status, tag, isAdmin, page, pageSize);
        return new KCResponse(ResponseCode.SUCCESS, "查询成功", list);
    }

    // 获取视频详情
    @GetMapping("/detail")
    public KCResponse detail(String id) {
        Video video = service.getVideoById(id);
        if (video != null) {
            video.setPv(String.valueOf(Integer.parseInt(video.getPv()) + 1));
            service.updateVideoPv(video);
            return new KCResponse(ResponseCode.SUCCESS, "查询成功", video);
        } else {
            return new KCResponse(ResponseCode.FAIL, "视频不存在");
        }
    }

    // 添加视频
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/create")
    @Transactional
    public KCResponse create(Video video) throws IOException {
        try {
            saveVideoFiles(video);
            service.createVideo(video);
            return new KCResponse(ResponseCode.SUCCESS, "创建成功");
        } catch (Exception e) {
            logger.error("创建视频失败", e);
            return new KCResponse(ResponseCode.FAIL, "创建失败");
        }
    }

    // 删除视频
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/delete")
    public KCResponse delete(String ids) {
        try {
            String[] arr = ids.split(",");
            for (String id : arr) {
                service.deleteVideo(id);
            }
            return new KCResponse(ResponseCode.SUCCESS, "删除成功");
        } catch (Exception e) {
            logger.error("删除视频失败", e);
            return new KCResponse(ResponseCode.FAIL, "删除失败");
        }
    }

    // 更新视频
    @RolePermission(level = RolePermissionLevel.ADMIN)
    @PostMapping("/update")
    @Transactional
    public KCResponse update(Video video) throws IOException {
        try {
            saveVideoFiles(video);
            service.updateVideo(video);
            return new KCResponse(ResponseCode.SUCCESS, "更新成功");
        } catch (Exception e) {
            logger.error("更新视频失败", e);
            return new KCResponse(ResponseCode.FAIL, "更新失败");
        }
    }

    // 保存视频文件与封面图片
    private void saveVideoFiles(Video video) throws IOException {
        // 保存封面图片
        String coverFileName = saveFile(video.getImageFile(), "image");
        if (!StringUtils.isEmpty(coverFileName)) {
            video.setCover(coverFileName);
        }

        // 保存视频文件
        String videoFileName = saveFile(video.getRawFile(), "raw");
        if (!StringUtils.isEmpty(videoFileName)) {
            video.setRaw(videoFileName);
        }
    }

    // 通用文件保存方法
    private String saveFile(MultipartFile file, String fileType) throws IOException {
        if (file != null && !file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID().toString() + fileExtension;
            String filePath = uploadPath + File.separator + fileType + File.separator + newFileName;

            File destFile = new File(filePath);
            if (!destFile.getParentFile().exists()) {
                destFile.getParentFile().mkdirs();
            }

            file.transferTo(destFile);
            return newFileName;
        }
        return null;
    }
}
