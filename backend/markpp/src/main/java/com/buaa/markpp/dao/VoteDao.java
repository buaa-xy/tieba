package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Vote;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoteDao {
    int sum(int commentId);
    void addVote(Vote vote);
    Vote selectByCommentAndUser(int commentId, int accountId);
    void delete(int voteId);
}
