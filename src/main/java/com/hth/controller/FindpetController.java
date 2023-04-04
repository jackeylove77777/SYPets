package com.hth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hth.dto.FindpetDetail;
import com.hth.dto.FosterDetail;
import com.hth.entity.Findpet;
import com.hth.entity.Foster;
import com.hth.entity.Msg;
import com.hth.entity.User;
import com.hth.mapper.UserMapper;
import com.hth.service.FindpetService;
import com.hth.service.FosterService;
import com.hth.service.StrayService;
import com.hth.util.BeanCopyUtils;
import com.hth.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.*;

@RestController
@RequestMapping("/findpet")
public class FindpetController {
    @Autowired
    FindpetService findpetService;
    @Autowired
    UserMapper userMapper;

    @DeleteMapping("/{id}")
    public Msg delete(@PathVariable Integer id){
        Findpet findpet = findpetService.getById(id);
        int uid = JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        if(uid!=findpet.getUid()){
            return Msg.fail("没有权限");
        }
        findpetService.removeById(id);
        return Msg.success("删除成功");
    }

    @RequiresRoles("admin")
    @PostMapping("/admin/changeStatus/{id}")
    public Msg changeStatus(@PathVariable Integer id){
        Findpet findpet = findpetService.getById(id);
        if(findpet.getStatus()==1){
            findpet.setStatus(0);
        }else{
            findpet.setStatus(1);
        }
        return Msg.success("修改状态成功");
    }
    @PostMapping("/solve/{id}")
    public Msg changeSolve(@PathVariable Integer id){
        Findpet findpet = findpetService.getById(id);
        if(findpet.getSolve()==1){
            findpet.setSolve(0);
        }else{
            findpet.setSolve(1);
        }
        return Msg.success("修改成功");
    }

    @DeleteMapping("/admin/{id}")
    @RequiresRoles("admin")
    public Msg adminDelete(@PathVariable Integer id){
        findpetService.removeById(id);
        return Msg.success("删除成功");
    }

    @GetMapping("/{id}")
    public Msg get(@PathVariable Integer id){
        Findpet findpet = findpetService.getById(id);
        return Msg.success().add("foster",findpet);
    }

    @PostMapping
    public Msg add(@RequestBody Findpet findpet){
        int uid = JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        findpet.setUid(uid);
        findpet.setPublishTime(new Date());
        findpetService.save(findpet);
        return Msg.success("发布成功");
    }

    @GetMapping("/all/{uid}")
    public Msg getByUserId(@PathVariable Integer uid){
        LambdaQueryWrapper<Findpet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Findpet::getUid,uid);
        List<Findpet> list = findpetService.list(queryWrapper);
        ArrayList<FindpetDetail> res = getDetail(list);

        return Msg.success().add("list",res);
    }

    @GetMapping("/all")
    public Msg getPage(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size,
                       @RequestParam(name ="query",defaultValue = "")String query){
        LambdaQueryWrapper<Findpet> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Findpet::getDescription,query);
        Page<Findpet> strayPage = new Page<>(page, size);
        Page<Findpet> page1 = findpetService.getBaseMapper().selectPage(strayPage,queryWrapper);
        ArrayList<FindpetDetail> res = getDetail(page1.getRecords());

        return Msg.success().add("list",res).add("total",page1.getTotal());
    }

    private ArrayList<FindpetDetail> getDetail(List<Findpet> lists){
        ArrayList<FindpetDetail> res = new ArrayList<>();
        //设置用户信息
        if(lists.size()>0){
            HashMap<Integer, User> hashMap = new HashMap<>();
            Set<Integer> ids = new HashSet<>();
            for(Findpet f : lists){
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
            for(Findpet f: lists){
                FindpetDetail findpetDetail = BeanCopyUtils.copyBean(f, FindpetDetail.class);
                User u = hashMap.get(findpetDetail.getUid());
                try {
                    findpetDetail.setUsername(u.getUsername());
                    findpetDetail.setAvatar(u.getAvatar());
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
                res.add(findpetDetail);
            }
        }
        return res;
    }
}
