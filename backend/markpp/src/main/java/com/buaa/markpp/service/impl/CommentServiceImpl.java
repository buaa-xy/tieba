package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.CommentDao;
import com.buaa.markpp.entity.Comment;
import com.buaa.markpp.service.CommentService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;

    public CommentServiceImpl(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Override
    public void addComment(Comment comment) {
        commentDao.insert(comment);
    }

    public List<Comment> selectByPostId(int postId) {
        return commentDao.selectByPostId(postId);
    }

    @Override
    public int count(int postId) {
        return commentDao.count(postId);
    }

    @Override
    public Comment getComment(int commentId) {
        return commentDao.select(commentId);
    }
}
