package com.buaa.markpp.service;

import com.buaa.markpp.entity.Follow;

import java.util.List;

public interface FollowService {

    int[] getFolloweeIdsByFollowerId(int followerId, int limit);

    int countFans(int authorId);

    int countFollows(int authorId);
    void add(Follow follow);
    Follow selectByAuthorAndFollower(int authorId, int followerId);
    void delete(int followId);
}
