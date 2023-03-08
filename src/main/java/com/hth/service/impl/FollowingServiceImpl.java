package com.hth.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hth.entity.PostDetail;
import com.hth.entity.User;
import com.hth.service.FollowingService;
import com.hth.service.PostService;
import com.hth.service.ProfileService;
import com.hth.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class FollowingServiceImpl implements FollowingService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    PostService postService;
    @Autowired
    ProfileService profileService;
    @Override
    public PageInfo findFollowingPost(Integer page, Integer size) {
        //找出用户的关注的所有人
        Set<Integer> members = redisTemplate.opsForSet().members(JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()) + ":followings");

        if(members.size()==0)return new PageInfo();
        //然后再找文章
        List<Integer> idList=new ArrayList<>(members);
        //mybatis分页
        PageHelper.startPage(page,size);
        List<PostDetail> followingPost = postService.findFollowingPost(idList);
        PageInfo pageInfo=new PageInfo(followingPost);
        return pageInfo;
    }

    @Override
    public List<User> findFowings() {
        return profileService.findFollowings(JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()));
    }
}
