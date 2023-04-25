package com.hth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hth.entity.PostDetail;
import com.hth.entity.User;
import com.hth.mapper.UserMapper;
import com.hth.service.PostService;
import com.hth.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
@Transactional
public class SearchServiceImpl implements SearchService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    PostService postService;
    @Override
    public Map<String, List> search(String content) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>();
        queryWrapper.select(User::getUsername,User::getAvatar,User::getId,User::getPrivateInfo);
        queryWrapper.like(User::getUsername,content);
        List<User> users = userMapper.selectList(queryWrapper);
        List<PostDetail> postDetails = postService.searchPost(content);
        Map<String,List> map=new HashMap<>();
        map.put("users",users);
        map.put("post",postDetails);
        return map;
    }
}
