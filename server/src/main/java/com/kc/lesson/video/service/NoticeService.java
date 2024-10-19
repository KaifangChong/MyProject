package com.kc.lesson.video.service;


import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Notice;

public interface NoticeService {
    PageInfo<Notice> getNoticeList(String keyword, int page, int pageSize);

    void createNotice(Notice notice);

    void deleteNotice(String id);

    void updateNotice(Notice notice);
}
