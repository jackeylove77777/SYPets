package com.hth.controller;

import com.hth.entity.Msg;
import com.hth.entity.Type;
import com.hth.log.NotUse;
import com.hth.log.TestSuccess;
import com.hth.service.TypeService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/type")
@TestSuccess
@Slf4j
public class TypeController {
    @Autowired
    TypeService typeService;


    @RequiresAuthentication
    @TestSuccess
    @ApiOperation(value = "文章类型列表",notes = "返回文章类型列表")
    @GetMapping("/postTypeList")
    public Msg findTypeList(){
        return Msg.success().add("typeList",typeService.findTypeList());
    }

    @RequiresAuthentication
    @ApiOperation(value = "文章类型列表",notes = "返回文章类型列表")
    @GetMapping("/postTypeNum")
    @TestSuccess
    public Msg findTypeNum(){
        List<Type> types = typeService.findTypeListNums();
        return Msg.success().add("typeList",types);
    }

    @RequiresAuthentication
    @GetMapping("/findTypeIdByName/{name}")
    @NotUse
    public Msg findTypeIdByName(@PathVariable String name){
        return Msg.success().add("type_id",typeService.findTypeIdByName(name));
    }
}
