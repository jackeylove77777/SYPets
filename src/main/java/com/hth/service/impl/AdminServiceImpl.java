package com.hth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hth.entity.User;
import com.hth.mapper.PostMapper;
import com.hth.mapper.UserMapper;
import com.hth.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {
    @Autowired
    PostMapper postMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public User findById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public PageInfo findAll(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<User> list = userMapper.selectList(new LambdaQueryWrapper<>());
        PageInfo pageInfo = new PageInfo(list);
        return pageInfo;
    }

    @Override
    public boolean changeStatus(Integer id) {
        User user = userMapper.selectById(id);
        if(user==null||user.getUsername().equals("admin"))return false;
        if(user.getStatus()==1)user.setStatus(0);
        else user.setStatus(1);
        userMapper.updateById(user);
        return true;
    }

    @Override
    public void solve(Integer id) {

    }

    @Override
    public void deleteSolve(Integer id) {

    }
}
