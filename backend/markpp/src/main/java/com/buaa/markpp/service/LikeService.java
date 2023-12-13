package com.buaa.markpp.service;

import com.buaa.markpp.entity.Like;

public interface LikeService {
    int sum(int postId);

    int sumByAuthor(int authorId);
    void add(Like like);
    Like selectByPostAndUser(int postId, int accountId);
    void delete(int likeId);
}
