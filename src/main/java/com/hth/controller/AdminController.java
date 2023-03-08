package com.hth.controller;

import com.github.pagehelper.PageInfo;
import com.hth.entity.Msg;
import com.hth.entity.PostDetail;
import com.hth.entity.User;
import com.hth.service.AdminService;
import com.hth.service.PostService;
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

    //找出所有的用户
    @RequiresRoles("admin")
    @GetMapping("/getUser")
    public Msg getUser(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        PageInfo all = adminService.findAll(page, size);
        return Msg.success().add("userlist",all);
    }

    //通过id搜索用户
    @RequiresRoles("admin")
    @GetMapping("/getOne/{id}")
    @ApiOperation("搜索一个用户")
    public Msg findOne(@PathVariable Integer id){
        User user = adminService.findById(id);
        List<User> userList = new ArrayList<>();
        if(user==null){
            userList.add(user);
        }
        return Msg.success().add("userlist",userList);
    }
    //修改用户状态
    @RequiresRoles("admin")
    @GetMapping("/changesState/{id}")
    public Msg changeStatus(@PathVariable Integer id){
        boolean b = adminService.changeStatus(id);
        return b?Msg.success("修改成功"):Msg.fail("修改失败");
    }
    //找出所有文章
    @RequiresRoles("admin")
    @GetMapping("/getPost")
    public Msg getPost(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size){
        return Msg.success("").add("postlist",postService.findAll(page, size));
    }
    //通过文章Id进行查找
    @RequiresRoles("admin")
    @GetMapping("/findOne/{id}")
    public Msg getOne(@PathVariable Integer id){
        PostDetail post = postService.findPostById(id);
        List<PostDetail> postList = new ArrayList<>();
        if(post!=null)postList.add(post);
        return Msg.success().add("postlist",postList);
    }
    //修改文章的状态
    @RequiresRoles("admin")
    @GetMapping("/changePostStatus/{id}")
    public Msg changePostStatus(@PathVariable Integer id){
        Boolean ok = postService.changeStatus(id);
        return ok?Msg.success("修改成功"):Msg.fail("修改失败");
    }


}
