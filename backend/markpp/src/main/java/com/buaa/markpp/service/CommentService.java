package com.buaa.markpp.service;

import com.buaa.markpp.entity.Comment;

import java.util.List;

public interface CommentService {
    void addComment(Comment comment);
    List<Comment> selectByPostId(int postId);
    int count(int postId);
    Comment getComment(int commentId);
}
