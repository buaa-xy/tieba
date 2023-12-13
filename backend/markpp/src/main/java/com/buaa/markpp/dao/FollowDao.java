package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface FollowDao {
    int countFans(int accountId);

    int countFollows(int accountId);

    int[] selectFolloweeIdsByFollowerId(@Param("followerId") int followerId, @Param("limit") int limit);

    void add(Follow follow);
    Follow selectByAuthorAndFollower(int authorId, int followerId);
    void delete(int followId);
}
