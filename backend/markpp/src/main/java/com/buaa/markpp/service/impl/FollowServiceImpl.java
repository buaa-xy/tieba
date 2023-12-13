package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.FollowDao;
import com.buaa.markpp.entity.Follow;
import com.buaa.markpp.service.FollowService;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {
    private final FollowDao followDao;

    public FollowServiceImpl(FollowDao followDao) {
        this.followDao = followDao;
    }

    @Override
    public int countFans(int authorId) {
        return followDao.countFans(authorId);
    }

    @Override
    public int[] getFolloweeIdsByFollowerId(int followerId, int limit) {
        return followDao.selectFolloweeIdsByFollowerId(followerId, limit);
    }

    @Override
    public void add(Follow follow) {
        followDao.add(follow);
    }

    @Override
    public Follow selectByAuthorAndFollower(int authorId, int followerId) {
        return followDao.selectByAuthorAndFollower(authorId, followerId);
    }

    @Override
    public void delete(int followId) {
        followDao.delete(followId);
    }

    @Override
    public int countFollows(int authorId) {
        return followDao.countFollows(authorId);
    }
}
