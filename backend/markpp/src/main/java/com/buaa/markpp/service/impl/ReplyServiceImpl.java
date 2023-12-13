package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.ReplyDao;
import com.buaa.markpp.entity.Reply;
import com.buaa.markpp.service.ReplyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {
    private final ReplyDao replyDao;

    public ReplyServiceImpl(ReplyDao replyDao) {
        this.replyDao = replyDao;
    }

    public List<Reply> selectByCmtId(int commentId) {
        return replyDao.selectByCmtId(commentId);
    }

    @Override
    public Reply selectById(int replyId) {
        return replyDao.selectById(replyId);
    }

    @Override
    public void add(Reply reply) {
        replyDao.add(reply);
    }
}
