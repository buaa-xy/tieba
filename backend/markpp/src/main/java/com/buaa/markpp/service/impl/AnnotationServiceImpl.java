package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.AnnotationDao;
import com.buaa.markpp.entity.Annotation;
import com.buaa.markpp.service.AnnotationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnotationServiceImpl implements AnnotationService {
    private final AnnotationDao annotationDao;

    public AnnotationServiceImpl(AnnotationDao annotationDao) {
        this.annotationDao = annotationDao;
    }

    @Override
    public void addAnnotation(Annotation annotation) {
        annotationDao.insert(annotation);
    }

    @Override
    public Annotation getAnnotation(int annotationId) {
        return annotationDao.select(annotationId);
    }

    @Override
    public List<Annotation> getAnnotationsByBlockId(int blockId) {
        return annotationDao.selectByBlockId(blockId);
    }
}
