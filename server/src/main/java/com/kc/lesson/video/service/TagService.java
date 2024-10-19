package com.kc.lesson.video.service;


import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Tag;

public interface TagService {
    PageInfo<Tag> getTagList(String tagName, int page, int pageSize);

    void createTag(Tag tag);

    void deleteTag(String id);

    void updateTag(Tag tag);
}
