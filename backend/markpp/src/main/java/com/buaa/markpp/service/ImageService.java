package com.buaa.markpp.service;

public interface ImageService {
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    boolean existsToken(String token);

    void addToken(String token);

    void removeToken(String token);
}
