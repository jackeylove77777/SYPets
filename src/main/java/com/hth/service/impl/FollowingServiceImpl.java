package com.hth.service.impl;


import com.github.pagehelper.PageInfo;
import com.hth.entity.User;
import com.hth.service.FollowingService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowingServiceImpl implements FollowingService {
    @Override
    public PageInfo findFollowingPost(Integer page, Integer size) {
        return null;
    }

    @Override
    public List<User> findFowings() {
        return null;
    }
}
