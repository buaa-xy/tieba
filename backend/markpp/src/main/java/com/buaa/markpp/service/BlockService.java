package com.buaa.markpp.service;

import com.buaa.markpp.entity.Block;
import com.buaa.markpp.model.BlockTree;

import java.util.List;

public interface BlockService {
    void addBlocks(int postId, BlockTree node, String content);
    void modifyBlocks(int postId, BlockTree node, String content, boolean annotationChg);
    Block getBlock(int blockId);
    List<Block> getBlocks(int postId);
}
