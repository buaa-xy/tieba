package com.buaa.markpp.service;

import com.buaa.markpp.entity.Favor;

public interface FavorService {
    void add(Favor favor);
    Favor selectByPostAndUser(int postId, int accountId);
    void delete(int favorId);
}
