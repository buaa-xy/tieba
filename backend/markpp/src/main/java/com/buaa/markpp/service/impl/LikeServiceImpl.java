package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.LikeDao;
import com.buaa.markpp.entity.Like;
import com.buaa.markpp.service.LikeService;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    private final LikeDao likeDao;

    public LikeServiceImpl(LikeDao likeDao) {
        this.likeDao = likeDao;
    }


    public int sum(int postId) {
        return likeDao.sum(postId);
    }

    @Override
    public int sumByAuthor(int authorId) {
        return likeDao.sumByAuthor(authorId);
    }

    @Override
    public void add(Like like) {
        likeDao.add(like);
    }

    @Override
    public Like selectByPostAndUser(int postId, int accountId) {
        return likeDao.selectByPostAndUser(postId, accountId);
    }

    @Override
    public void delete(int likeId) {
        likeDao.delete(likeId);
    }
}
