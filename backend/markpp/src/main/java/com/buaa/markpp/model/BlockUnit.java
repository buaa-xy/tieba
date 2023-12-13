package com.buaa.markpp.model;

import com.buaa.markpp.entity.Block;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Service
@Getter
public class BlockUnit {
    private Block block;
    List<AnnotationInfo> annotationInfos;
}
