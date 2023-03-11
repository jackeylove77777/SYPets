package com.hth.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hth.entity.Foster;
import com.hth.entity.Msg;
import com.hth.entity.Stray;
import com.hth.service.FosterService;
import com.hth.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/foster")
public class FosterController {
    @Autowired
    FosterService fosterService;

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

    @DeleteMapping("/admin/{id}")
    @RequiresRoles("admin")
    public Msg adminDelete(@PathVariable Integer id){
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

    @GetMapping("/all")
    public Msg getPage(@RequestParam(name = "pagenum",defaultValue = "1")Integer page,
                       @RequestParam(name ="pagesize",defaultValue = "5")Integer size){

        Page<Foster> strayPage = new Page<>(page, size);
        Page<Foster> page1 = fosterService.page(strayPage);

        return Msg.success().add("fosterpage",page1);
    }
}
