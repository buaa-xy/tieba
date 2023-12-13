package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.VoteDao;
import com.buaa.markpp.entity.Vote;
import com.buaa.markpp.service.VoteService;
import org.springframework.stereotype.Service;

@Service
public class VoteServiceImpl implements VoteService {
    private final VoteDao voteDao;

    public VoteServiceImpl(VoteDao voteDao) {
        this.voteDao = voteDao;
    }

    public int sum(int commentId) {
        return voteDao.sum(commentId);
    }

    @Override
    public void addVote(Vote vote) {
        voteDao.addVote(vote);
    }

    @Override
    public Vote selectByCommentAndUser(int commentId, int accountId) {
        return voteDao.selectByCommentAndUser(commentId, accountId);
    }

    @Override
    public void delete(int voteId) {
        voteDao.delete(voteId);
    }
}