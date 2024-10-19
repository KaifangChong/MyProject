package com.kc.lesson.video.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Classification;
import com.kc.lesson.video.mapper.ClassificationMapper;
import com.kc.lesson.video.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassificationServiceImpl implements ClassificationService {

    @Autowired
    ClassificationMapper mapper;

    @Override
    public PageInfo<Classification> getClassificationList(String cName, int page, int pageSize) {
        // 设置分页参数
        PageHelper.startPage(page, pageSize);
        List<Classification> list = mapper.selectAll(cName);
        // 使用 PageInfo 封装分页信息
        return new PageInfo<>(list);
    }

    @Override
    public void createClassification(Classification classification) {
        System.out.println(classification);
        classification.setCreateTime(String.valueOf(System.currentTimeMillis()));
        mapper.insert(classification);
    }

    @Override
    public void deleteClassification(String id) {
        mapper.deleteById(id);
    }

    @Override
    public void updateClassification(Classification classification) {
        mapper.updateById(classification);
    }
}
