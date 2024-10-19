package com.kc.lesson.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Tag;
import com.kc.lesson.video.mapper.TagMapper;
import com.kc.lesson.video.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    TagMapper mapper;

    @Override
    public PageInfo<Tag> getTagList(String tagName,int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        List<Tag> list = mapper.selectAll(tagName);
        // 使用 PageInfo 封装分页信息
        return new PageInfo<>(list);
    }

    @Override
    public void createTag(Tag tag) {
        System.out.println(tag);
        tag.setCreateTime(String.valueOf(System.currentTimeMillis()));
        mapper.insert(tag);
    }

    @Override
    public void deleteTag(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateTag(Tag tag) {
        mapper.updateById(tag);
    }
}
