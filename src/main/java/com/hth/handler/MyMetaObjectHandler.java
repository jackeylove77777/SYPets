package com.hth.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.hth.util.JWTUtil;

import io.swagger.models.auth.In;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        Integer userId= JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        System.out.println(1111111);
        this.setFieldValByName("create_time", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        Integer userId= JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        this.setFieldValByName("updateTime", new Date(), metaObject);

    }
}