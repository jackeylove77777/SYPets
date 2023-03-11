package com.hth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hth.entity.Msg;
import com.hth.entity.Stray;
import com.hth.service.StrayService;
import com.hth.util.JWTUtil;
import io.swagger.models.auth.In;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/stray")
public class StrayController {
    @Autowired
    StrayService strayService;

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

    @GetMapping("/all")
    public Msg getPage(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size){

        Page<Stray> strayPage = new Page<>(page, size);
        Page<Stray> page1 = strayService.page(strayPage);

        return Msg.success().add("starypage",page1);
    }
}
