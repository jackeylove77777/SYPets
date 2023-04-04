package com.hth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hth.dto.FindpetDetail;
import com.hth.dto.FosterDetail;
import com.hth.dto.StaryDetail;
import com.hth.entity.*;
import com.hth.mapper.UserMapper;
import com.hth.service.StrayService;
import com.hth.util.BeanCopyUtils;
import com.hth.util.JWTUtil;
import io.swagger.models.auth.In;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/stray")
public class StrayController {
    @Autowired
    StrayService strayService;
    @Autowired
    UserMapper userMapper;
    @DeleteMapping("/{id}")
    public Msg delete(@PathVariable Integer id){
        Stray stray = strayService.getById(id);
        int uid = JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        if(uid!=stray.getUid()){
            return Msg.fail("没有权限");
        }
        strayService.removeById(id);
        return Msg.success("删除成功");
    }

    @RequiresRoles("admin")
    @PostMapping("/admin/changeStatus/{id}")
    public Msg changeStatus(@PathVariable Integer id){
        Stray stray = strayService.getById(id);
        if(stray.getStatus()==1){
            stray.setStatus(0);
        }else{
            stray.setStatus(1);
        }
        return Msg.success("修改状态成功");
    }

    @DeleteMapping("/admin/{id}")
    @RequiresRoles("admin")
    public Msg adminDelete(@PathVariable Integer id){
        strayService.removeById(id);
        return Msg.success("删除成功");
    }

    @GetMapping("/{id}")
    public Msg get(@PathVariable Integer id){
        Stray stray = strayService.getById(id);
        return Msg.success().add("stray",stray);
    }

    @PostMapping
    public Msg add(@RequestBody Stray stray){
        int uid = JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        stray.setUid(uid);
        stray.setPublishTime(new Date());
        strayService.save(stray);
        return Msg.success("发布成功");
    }

    @GetMapping("/all/{uid}")
    public Msg getByUserId(@PathVariable Integer uid){
        LambdaQueryWrapper<Stray> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Stray::getUid,uid);
        List<Stray> list = strayService.list(queryWrapper);
        ArrayList<StaryDetail> res = getDetail(list);

        return Msg.success().add("list",res);
    }

    @GetMapping("/all")
    public Msg getPage(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size,
                       @RequestParam(name ="query",defaultValue = "")String query){
        LambdaQueryWrapper<Stray> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Stray::getDescription,query);
        Page<Stray> strayPage = new Page<>(page, size);
        Page<Stray> page1 = strayService.getBaseMapper().selectPage(strayPage,queryWrapper);
        ArrayList<StaryDetail> res = getDetail(page1.getRecords());

        return Msg.success().add("list",res).add("total",page1.getTotal());
    }

    private ArrayList<StaryDetail> getDetail(List<Stray> lists){
        ArrayList<StaryDetail> res = new ArrayList<>();
        //设置用户信息
        if(lists.size()>0){
            HashMap<Integer, User> hashMap = new HashMap<>();
            Set<Integer> ids = new HashSet<>();
            for(Stray f : lists){
                ids.add(f.getUid());
            }
            LambdaQueryWrapper<User> userQueryWrapper = new LambdaQueryWrapper<>();
            userQueryWrapper.select(User::getUsername,User::getAvatar,User::getId);//只选择头像和名字，减少数据库传输的数据量
            userQueryWrapper.in(User::getId,ids);
            List<User> users = userMapper.selectList(userQueryWrapper);
            for(User user:users){
                hashMap.put(user.getId(),user);

            }
            //整合Foster和User，返回FosterDetail
            for(Stray f: lists){
                StaryDetail staryDetail = BeanCopyUtils.copyBean(f, StaryDetail.class);
                User u = hashMap.get(staryDetail.getUid());
                try {
                    staryDetail.setUsername(u.getUsername());
                    staryDetail.setAvatar(u.getAvatar());
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                res.add(staryDetail);
            }
        }
        return res;
    }
}
