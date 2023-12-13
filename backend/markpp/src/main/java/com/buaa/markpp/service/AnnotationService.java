package com.buaa.markpp.service;

import com.buaa.markpp.entity.Annotation;

import java.util.List;

public interface AnnotationService {
    void addAnnotation(Annotation annotation);
    Annotation getAnnotation(int annotationId);
    List<Annotation> getAnnotationsByBlockId(int block);
}
