package com.buaa.markpp.service.impl;

import com.buaa.markpp.dao.FavorDao;
import com.buaa.markpp.entity.Favor;
import com.buaa.markpp.service.FavorService;
import org.springframework.stereotype.Service;

@Service
public class FavorServiceImpl implements FavorService {
    private final FavorDao favorDao;

    public FavorServiceImpl(FavorDao favorDao) {
        this.favorDao = favorDao;
    }

    @Override
    public void add(Favor favor) {
        favorDao.add(favor);
    }

    @Override
    public Favor selectByPostAndUser(int postId, int accountId) {
        return favorDao.selectByPostAndUser(postId, accountId);
    }

    @Override
    public void delete(int favorId) {
        favorDao.delete(favorId);
    }
}
