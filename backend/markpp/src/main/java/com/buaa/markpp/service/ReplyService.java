package com.buaa.markpp.service;

import com.buaa.markpp.entity.Reply;

import java.util.List;

public interface ReplyService {
    List<Reply> selectByCmtId(int commentId);
    Reply selectById(int replyId);
    void add(Reply reply);
}
