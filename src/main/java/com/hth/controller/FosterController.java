package com.hth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hth.dto.FindpetDetail;
import com.hth.dto.FosterDetail;
import com.hth.entity.*;
import com.hth.mapper.UserMapper;
import com.hth.service.FosterService;
import com.hth.util.BeanCopyUtils;
import com.hth.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/foster")
public class FosterController {
    @Autowired
    FosterService fosterService;
    @Autowired
    UserMapper userMapper;
    @DeleteMapping("/{id}")
    public Msg delete(@PathVariable Integer id){
        Foster foster = fosterService.getById(id);
        int uid = JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        if(uid!=foster.getUid()){
            return Msg.fail("没有权限");
        }
        fosterService.removeById(id);
        return Msg.success("删除成功");
    }
    @RequiresRoles("admin")
    @PostMapping("/admin/changeStatus/{id}")
    public Msg changeStatus(@PathVariable Integer id){
        Foster foster = fosterService.getById(id);
        if(foster.getStatus()==1){
            foster.setStatus(0);
        }else{
            foster.setStatus(1);
        }
        return Msg.success("修改状态成功");
    }
    @GetMapping("/solve/{id}")
    public Msg changeSolve(@PathVariable Integer id){
        Foster foster = fosterService.getById(id);
        if(foster.getSolve()==1){
            foster.setSolve(0);
        }else{
            foster.setSolve(1);
        }
        return Msg.success("修改状态成功");
    }

    @DeleteMapping("/admin/{id}")
    @RequiresRoles("admin")
    public Msg adminFosterDelete(@PathVariable Integer id){
        fosterService.removeById(id);
        return Msg.success("删除成功");
    }

    @GetMapping("/{id}")
    public Msg get(@PathVariable Integer id){
        Foster foster = fosterService.getById(id);
        return Msg.success().add("foster",foster);
    }

    @PostMapping
    public Msg add(@RequestBody Foster foster){
        int uid = JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        foster.setUid(uid);
        foster.setPublishTime(new Date());
        fosterService.save(foster);
        return Msg.success("发布成功");
    }

    @GetMapping("/all/{uid}")
    public Msg getByUserId(@PathVariable Integer uid){
        LambdaQueryWrapper<Foster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Foster::getUid,uid);
        List<Foster> list = fosterService.list(queryWrapper);
        ArrayList<FosterDetail> res = getDetail(list);

        return Msg.success().add("list",res);
    }

    @GetMapping("/all")
    public Msg getPage(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size,
                       @RequestParam(name ="query",defaultValue = "")String query){
        LambdaQueryWrapper<Foster> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Foster::getDescription,query);
        Page<Foster> strayPage = new Page<>(page, size);
        Page<Foster> page1 = fosterService.getBaseMapper().selectPage(strayPage,queryWrapper);
        ArrayList<FosterDetail> res = getDetail(page1.getRecords());

        return Msg.success().add("list",res).add("total",page1.getTotal());
    }

    private ArrayList<FosterDetail> getDetail(List<Foster> lists){
        ArrayList<FosterDetail> res = new ArrayList<>();
        //设置用户信息
        if(lists.size()>0){
            HashMap<Integer, User> hashMap = new HashMap<>();
            Set<Integer> ids = new HashSet<>();
            for(Foster f : lists){
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
            for(Foster f: lists){
                FosterDetail fosterDetail = BeanCopyUtils.copyBean(f, FosterDetail.class);
                User u = hashMap.get(fosterDetail.getUid());
                try {
                    fosterDetail.setUsername(u.getUsername());
                    fosterDetail.setAvatar(u.getAvatar());
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                res.add(fosterDetail);
            }
        }
        return res;
    }
}
