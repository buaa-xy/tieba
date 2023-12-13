package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentDao {
    void insert(Comment comment);
    List<Comment> selectByPostId(int postId);
    int count(int postId);
    Comment select(int commentId);
}
