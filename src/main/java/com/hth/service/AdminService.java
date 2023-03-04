package com.hth.service;

import com.github.pagehelper.PageInfo;
import com.hth.entity.User;

import java.util.List;

public interface AdminService {
    User findById(Integer id);
    PageInfo findAll(Integer page, Integer size);

    boolean changeStatus(Integer id);


    void solve(Integer id);

    void deleteSolve(Integer id);
}
