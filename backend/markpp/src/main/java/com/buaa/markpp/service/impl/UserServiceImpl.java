package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.AccountDao;
import com.buaa.markpp.entity.Account;
import com.buaa.markpp.model.ExceptionType;
import com.buaa.markpp.model.MarkppException;
import com.buaa.markpp.model.UserInfo;
import com.buaa.markpp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final AccountDao accountDao;

    public UserServiceImpl(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void addUser(UserInfo user) {
        if (accountDao.selectByUsername(user.getUsername()) != null) {
            throw new MarkppException("用户名已存在");
        }
        Account account = new Account();
        account.setUsername(user.getUsername());
        account.setPassword(user.getPassword());
        accountDao.insert(account);
    }

    @Override
    public Account getAccountByName(String username) {
        Account account = accountDao.selectByUsername(username);
        if (account == null)
            throw new MarkppException(ExceptionType.NOT_FOUND);
        return account;
    }

    @Override
    public Account getAccountById(int id) {
        Account account = accountDao.selectById(id);
        if (account == null)
            throw new MarkppException(ExceptionType.NOT_FOUND);
        return account;
    }

    @Override
    public void update(UserInfo userInfo) {
        Account account = new Account();
        account.setAccountId(userInfo.getUserId());
        account.setNickname(userInfo.getNickname());
        account.setPassword(userInfo.getPassword());
        account.setAvatar(userInfo.getAvatar());
        account.setEmail(userInfo.getEmail());
        accountDao.update(account);
    }

    @Override
    public List<Account> getAccountByHeat(int number) {
        return accountDao.selectSortedByHeat(number);
    }
}
