package com.hth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hth.dto.FindPwdSendEmailDto;
import com.hth.dto.RegisterDto;
import com.hth.dto.UpdatePasswordDto;
import com.hth.entity.LimitIp;
import com.hth.entity.User;
import com.hth.mapper.UserMapper;
import com.hth.service.EmailService;
import com.hth.service.InteractionService;
import com.hth.service.UserService;
import com.hth.util.BeanCopyUtils;
import com.hth.util.IpLimitUtil;
import com.hth.util.JWTUtil;
import com.hth.util.SaltUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    InteractionService interactionService;
    @Lazy
    @Autowired
    RedisTemplate redisTemplate;
    @Lazy
    @Autowired
    EmailService emailService;
    @Lazy
    @Autowired
    IpLimitUtil ipLimitUtil;
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
            newUser.setSalt(salt);
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

    @Override
    public User findUserByUserName(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }

    /**
     * 找出用户的详细信息  例如粉丝、关注、收藏数量
     * @param username
     * @return
     */
    @Override
    public User findUserDetails(String username) {
        User user=this.findUserByUserName(username);
        if(user==null) return null;
        user.setPassword(null);
        user.setSalt(null);
        user.setProfile(interactionService.findUserProfile(user.getId()));
        return user;
    }

    @Override
    public List<User> findFollowerSimpleInfo(List<Integer> list) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getId,list);
        //只要id，用户名，头像这三个字段
        queryWrapper.select(User::getId,User::getUsername,User::getAvatar);
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public boolean updateUserName(String userName) {
        int exists = this.exists(userName);
        if(exists==0){//如果要修改的用户名没被使用
            Integer userId= JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
            User user=userMapper.selectById(userId);
            user.setUsername(userName);
            userMapper.updateById(user);
            return true;
        }
        return false;
    }
    @Override
    public void updateSex(Integer sex) {
        Integer userId= JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        User user=userMapper.selectById(userId);
        user.setSex(sex);
        userMapper.updateById(user);
    }

    /**
     * 修改个人主页的简介
     * @param info
     */
    @Override
    public void updateInfo(String info) {
        Integer userId= JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        User user=userMapper.selectById(userId);
        user.setPrivateInfo(info);
        userMapper.updateById(user);
    }

    /**
     * 修改密码
     * @param dto
     * @return
     */
    @Override
    public Map<String,String> updatePassword(UpdatePasswordDto dto) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        User user=userMapper.selectById(userId);
        //获取用户提交的旧密码
        String lastPassword = dto.getLastPassword();
        //将提交的旧密码与盐值一起进行md5加密
        Md5Hash md5Hash=new Md5Hash(lastPassword,user.getSalt(),1024);
        lastPassword=md5Hash.toHex();
        Map<String,String> map=new HashMap<>();
        //将md5加密后的密码与数据库存的密码进行比对，如果相同就进入修改
        if(user.getPassword().equals(lastPassword)){
            //验证两次新密码是否相同，前端已经验证过一遍了
            if(dto.getNewPassword().equals(dto.getSecondPassword())){
                //重新生成盐值，将新md5加密的新密码存入数据库
                String salt=SaltUtil.getSalt(5);
                String newPassword=new Md5Hash(dto.getNewPassword(),salt,1024).toHex();
                user.setSalt(salt);
                user.setPassword(newPassword);
                userMapper.updateById(user);
                map.put("message","success");
            }
            else{
                map.put("message","两次密码不一致");
            }
        }
        else map.put("message","旧密码错误");
        return map;
    }

    /**
     * 找回密码
     * @param dto
     * @param ip
     * @return
     */
    public Map<String, String> findPassword(FindPwdSendEmailDto dto,String ip) {
        //限流
        ArrayList<LimitIp> ipArrayList=new ArrayList<>();
        ipArrayList.add(new LimitIp(ip+"findPsw",60*60,5,"一小时"));
        ipArrayList.add(new LimitIp(ip+"findPsw:count",60,1,"一分钟"));
        Map<String, String> msg = ipLimitUtil.ipContro(ipArrayList);
        if(!msg.get("message").equals("success"))
            return msg;

        Map<String,String> map=new HashMap<>();
        User user=this.findUserByUserName(dto.getUsername());
        if(user==null){
            map.put("message","用户不存在");
            return map;
        }
        if(!user.getEmail().equals(dto.getEmail())){
            map.put("message","输入邮箱与该用户绑定邮箱不一致");
            return map;
        }
        //生成修改密码的邮箱验证码
        String newPsw=SaltUtil.getSalt(8);
        //发送邮件
        emailService.sendPsw(dto.getEmail(),newPsw);

        String newSalt=SaltUtil.getSalt(6);
        newPsw=new Md5Hash(newPsw,newSalt,1024).toHex();

        user.setSalt(newSalt);
        user.setPassword(newPsw);

        //修改密码
        userMapper.updateById(user);
        map.put("message","success");
        return map;
    }

    @Override
    public void delete(Integer id) {
        userMapper.deleteById(id);
    }
}
