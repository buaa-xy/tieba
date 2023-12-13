package com.buaa.markpp.service;

import com.buaa.markpp.entity.Vote;

public interface VoteService {
    int sum(int commentId);
    void addVote(Vote vote);
    Vote selectByCommentAndUser(int commentId, int accountId);
    void delete(int voteId);
}
