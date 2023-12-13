package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Like;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeDao {
    int sum(int postId);

    int sumByAuthor(int accountId);
    void add(Like like);
    Like selectByPostAndUser(int postId, int accountId);
    void delete(int likeId);
}
