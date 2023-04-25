package com.hth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hth.entity.Foster;
import com.hth.entity.Msg;
import com.hth.entity.PostDetail;
import com.hth.entity.User;
import com.hth.log.TestSuccess;
import com.hth.mapper.PostMapper;
import com.hth.mapper.UserMapper;
import com.hth.service.AdminService;
import com.hth.service.PostService;
import com.hth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/admin")
@RestController
@Api(value = "管理员接口",tags = {"管理员接口"})
public class AdminController {
    @Autowired
    AdminService adminService;
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    //找出所有的用户
    @RequiresRoles("admin")
    @GetMapping("/getUser")
    @TestSuccess
    public Msg getUser(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size,
                       @RequestParam(name="query",defaultValue = "")String query){
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(User::getUsername,query);
        Page<User> strayPage = new Page<>(page, size);
        Page<User> page1 = userMapper.selectPage(strayPage,queryWrapper);
        return Msg.success().add("list",page1.getRecords()).add("total",page1.getTotal());
    }
    //通过Id删除用户
    @RequiresRoles("admin")
    @DeleteMapping("/user/{id}")
    @TestSuccess
    public Msg delUser(@PathVariable Integer id){
        userService.delete(id);
        return Msg.success("成功删除");
    }

    //修改用户状态
    @RequiresRoles("admin")
    @GetMapping("/changeStatus/{id}")
    @TestSuccess
    public Msg changeStatus(@PathVariable Integer id){
        boolean b = adminService.changeStatus(id);
        return b?Msg.success("修改成功"):Msg.fail("修改失败");
    }
    //找出所有文章
    @RequiresRoles("admin")
    @GetMapping("/getPost")
    public Msg getPost(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size,
                       @RequestParam(name="query",defaultValue = "")String query){
        List<PostDetail> list = postService.searchPost(query);
        PageHelper.startPage(page,size);
        PageInfo pageInfo=new PageInfo(list);
        System.out.println(list);
        return Msg.success().add("list",list).add("total",pageInfo.getTotal());
    }
    //修改文章的状态
    @RequiresRoles("admin")
    @GetMapping("/changePostStatus/{id}")
    public Msg changePostStatus(@PathVariable Integer id){
        Boolean ok = postService.changeStatus(id);
        return ok?Msg.success("修改成功"):Msg.fail("修改失败");
    }

    //删除文章
    @RequiresRoles("admin")
    @DeleteMapping("/post/{id}")
    public Msg deletePostById(@PathVariable Integer id){
        postService.adminDelete(id);
        return Msg.success("成功删除");
    }
}
