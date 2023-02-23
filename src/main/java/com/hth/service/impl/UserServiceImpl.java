package com.hth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hth.dto.RegisterDto;
import com.hth.entity.User;
import com.hth.mapper.UserMapper;
import com.hth.service.UserService;
import com.hth.util.BeanCopyUtils;
import com.hth.util.SaltUtil;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public Map<String,String> register(RegisterDto registerDto) {
        Map<String,String> map=new HashMap<>();
        if(!registerDto.getPassword().equals(registerDto.getSecondPassword())){
            map.put("message","两次密码不一致");
            return map;
        }
        //获取验证码
        String code = (String) redisTemplate.opsForValue().get(registerDto.getEmail() + ":code");

        if(code==null||code.equals("")){
            map.put("message","验证码已过期");
            return map;
        }

        if(!code.equals(registerDto.getCode())) {
            map.put("message", "验证码不正确");
            return map;
        }
        if(exists(registerDto.getUsername())==0){//查看用户名是否已经存在，如果没存在
            //生产随机的salt
            String salt= SaltUtil.getSalt(5);
            //加密方式
            Md5Hash md5Hash=new Md5Hash(registerDto.getPassword(),salt,1024);
            String password=md5Hash.toHex();
            //默认用户头像
            String avatar="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png";
            registerDto.setPassword(password);
            User newUser = BeanCopyUtils.copyBean(registerDto,User.class);
            newUser.setAvatar(avatar);
            userMapper.insert(newUser);
            //删除验证秒
            redisTemplate.delete(registerDto.getEmail() + ":code");
            map.put("message","注册成功");
            return map;
        }
        map.put("message","用户已存在");
        return map;
    }

    @Override
    public int exists(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(!Objects.isNull(username),User::getUsername,username);
        return userMapper.selectCount(wrapper);
    }
}
