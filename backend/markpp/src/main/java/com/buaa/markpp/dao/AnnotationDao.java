package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Annotation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AnnotationDao {
    void insert(Annotation annotation);
    Annotation select(int annotationId);
    List<Annotation> selectByBlockId(int blockId);
    void replaceBlockId(int newId, int originalId);
}
