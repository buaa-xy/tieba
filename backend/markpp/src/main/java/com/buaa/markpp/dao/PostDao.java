package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface PostDao {
    void insert(Post post);
    Post select(int postId);
    List<Post> selectByAccountId(int accountId);
    void update(Post post);
    List<Post> search(String keyword);
    List<Post> selectSortedByTime(int number);
    List<Post> selectSortedByHeat(int number);

    List<Post> selectFavorites(int uerId);
}
