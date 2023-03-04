package com.hth.controller;

import com.hth.entity.Msg;
import com.hth.service.TypeService;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/type")
public class TypeController {
    @Autowired
    TypeService typeService;
    @RequiresAuthentication
    @ApiOperation(value = "文章类型列表",notes = "返回文章类型列表")
    @GetMapping("/postTypeList")
    public Msg findTypeList(){
        return Msg.success().add("typeList",typeService.findTypeList());
    }

    @RequiresAuthentication
    @ApiOperation(value = "文章类型列表",notes = "返回文章类型列表")
    @GetMapping("/postTypeNum")
    public Msg findTypeNum(){
        return Msg.success().add("typeList",typeService.findTypeListNums());
    }

    @RequiresAuthentication
    @GetMapping("/findTypeIdByName/{name}")
    public Msg findTypeIdByName(@PathVariable String name){
        return Msg.success().add("type_id",typeService.findTypeIdByName(name));
    }
}
