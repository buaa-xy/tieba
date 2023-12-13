package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Favor;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FavorDao {
    void add(Favor favor);
    Favor selectByPostAndUser(int postId, int accountId);
    void delete(int favorId);
}
