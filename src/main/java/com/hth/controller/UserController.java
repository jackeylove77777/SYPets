package com.hth.controller;

import com.hth.dto.RegisterDto;
import com.hth.entity.Msg;
import com.hth.log.Logweb;
import com.hth.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation(value = "用户注册", notes = "填写用户名，密码")
    @PostMapping("/register")
    public Msg register(@Validated @RequestBody RegisterDto registerDto)
    {
        Map<String,String> info= userService.register(registerDto);
        String msg=info.get("message");
        if (msg.equals("注册成功"))
            return Msg.success(msg);
        return Msg.fail(msg);
    }
}
