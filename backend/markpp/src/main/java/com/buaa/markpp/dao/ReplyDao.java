package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Reply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyDao {
    List<Reply> selectByCmtId(int commentId);
    Reply selectById(int replyId);
    void add(Reply reply);
}
