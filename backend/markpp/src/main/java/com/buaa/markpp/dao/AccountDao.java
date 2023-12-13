package com.buaa.markpp.dao;

import com.buaa.markpp.entity.Account;
import com.buaa.markpp.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountDao {

    Account selectById(int id);

    Account selectByUsername(String username);

    void insert(Account account);

    void update(Account account);

    List<Account> selectSortedByHeat(int number);
}
