package com.buaa.markpp.service;

import com.buaa.markpp.entity.Account;
import com.buaa.markpp.model.UserInfo;

import java.util.List;

public interface UserService {
    /**
     * Add a user
     * @throws com.buaa.markpp.model.MarkppException if username duplicates
     */
    void addUser(UserInfo user);

    Account getAccountByName(String username);

    Account getAccountById(int id);

    void update(UserInfo userInfo);

    List<Account> getAccountByHeat(int number);
}
