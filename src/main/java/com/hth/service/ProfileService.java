package com.hth.service;

import com.hth.entity.User;

import java.util.List;
import java.util.Map;

public interface ProfileService {
    boolean isFollower(Integer user1,Integer user2);
    boolean follow(Integer userId);

    boolean unFollow(Integer userId);

    Map findCollectById(Integer id);
    List<User> findFollowers(Integer id);

    List<User> findFollowings(Integer id);
}