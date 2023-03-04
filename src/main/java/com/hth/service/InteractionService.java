package com.hth.service;

import com.hth.entity.Profile;

public interface InteractionService {
    int onStart(Integer postId);

    int unStart(Integer postId);

    boolean existsStar(Integer postId);

    int onCollect(Integer postId);

    boolean existsCollect(Integer postId);

    int unCollect(Integer postId);

    Profile findUserProfile(Integer userId);

}