package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Block;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlockDao {
    void insert(Block block);
    Block select(int blockId);
    Block selectBlockRoot(int postId);
    void deleteByBlockId(int blockId);
}
