package com.hth.controller;

import com.hth.dto.FindPwdSendEmailDto;
import com.hth.dto.LoginDto;
import com.hth.dto.RegisterDto;
import com.hth.dto.UpdatePasswordDto;
import com.hth.entity.Msg;
import com.hth.entity.User;
import com.hth.log.Logweb;
import com.hth.service.UserService;
import com.hth.util.IpUtil;
import com.hth.util.JWTUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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

    @Logweb("用户登录")
    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Msg login(@RequestBody @Validated LoginDto logindto){
        User user = userService.findUserByUserName(logindto.getUsername());
        if(user==null){
            return Msg.fail("用户名不存在");
        }
        if(user.getStatus()==0)return Msg.fail("用户呗锁定");
        String password = logindto.getPassword();
        //加密方式 md5+哈希散列1024次
        Md5Hash md5Hash=new Md5Hash(password,user.getSalt(),1024);
        password=md5Hash.toHex();
        if(password.equals(user.getPassword())){
            user.setPassword(null);
            user.setSalt(null);
            //成功反回token
            return Msg.success("登录成功").add("token", JWTUtil.createToken(user.getId())).add("user",user);
        }
        return Msg.fail("密码错误");
    }

    @GetMapping("/getAvatar/{username}")
    public String getAvatar(@PathVariable String username){
        User user = userService.findUserByUserName(username);
        return user.getAvatar();
    }
    @GetMapping("/findUserDetails/{username}")
    public Msg findUserDetails(@PathVariable String username){
        return Msg.success().add("user",userService.findUserDetails(username));
    }

    @Logweb("修改用户名")
    @GetMapping("/updateUserName")
    public Msg updateUserName(@RequestParam(required = true) String userName){
        if(userName!=null&&userName.length()>3&&userName.length()<20){
            boolean ok = userService.updateUserName(userName);
            if(ok){
                return Msg.success("修改成功");
            }else return Msg.fail("用户名已经存在");
        }
        return Msg.fail("格式错误");
    }

    @GetMapping("/updateSex")
    public Msg updateSex(@RequestParam(required = true) Integer sex){
        if(sex==1||sex==2){
            userService.updateSex(sex);
            return Msg.success("修改成功");
        }
        return Msg.success("输入有误");
    }
    @Logweb("修改个人简介")
    @GetMapping("/updateInfo")
    public Msg  updateInfo(@RequestParam(required = true) String info){
        userService.updateInfo(info);
        return Msg.success("修改成功");
    }
    @Logweb("修改密码")
    @PostMapping("/updatePassword")
    public Msg updatePassword(@RequestBody @Validated UpdatePasswordDto dto) {
        Map<String, String> map = userService.updatePassword(dto);
        String message = map.get("message");
        if (message.equals("success"))
            return Msg.success(message);
        return Msg.fail(message);
    }

    @PostMapping("/findPassword")
    public Msg findPassword(@RequestBody @Validated FindPwdSendEmailDto findPwdSendEmailDto,HttpServletRequest request)throws MessagingException{
        String ip = IpUtil.getIpAddr(request);
        Map<String,String> msg = userService.findPassword(findPwdSendEmailDto,ip);
        if(msg.get("message").equals("success")){
            return Msg.success("邮件已发送");
        }else{
            return Msg.fail(msg.get("message"));
        }
    }
    /**
     * 就是config配置类FilterExceConfig 认证异常的捕获
     * @param request
     * @return
     */
    @Logweb("认证失败")
    @RequestMapping("/error/exthrow")
    public Map<String, Object> rethrow(HttpServletRequest request) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("code", "401");
        map.put("message", "token认证失败");
        return map;
    }
}
