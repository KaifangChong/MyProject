package com.kc.lesson.video.service;


import com.github.pagehelper.PageInfo;
import com.kc.lesson.video.entity.Classification;

public interface ClassificationService {
    PageInfo<Classification> getClassificationList(String cName, int page, int pageSize);

    void createClassification(Classification Classification);

    void deleteClassification(String id);

    void updateClassification(Classification Classification);
}
