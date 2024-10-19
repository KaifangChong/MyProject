package com.kc.lesson.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Notice;
import com.kc.lesson.video.mapper.NoticeMapper;
import com.kc.lesson.video.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    NoticeMapper mapper;

    @Override
    public PageInfo<Notice> getNoticeList(String keyword, int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        List<Notice> list = mapper.selectAll(keyword);
        // 使用 PageInfo 封装分页信息
        return new PageInfo<>(list);
    }

    @Override
    public void createNotice(Notice notice) {
        System.out.println(notice);
        notice.setCreateTime(String.valueOf(System.currentTimeMillis()));
        mapper.insert(notice);
    }

    @Override
    public void deleteNotice(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateNotice(Notice notice) {
        mapper.updateById(notice);
    }
}
