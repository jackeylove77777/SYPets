package com.hth.shiro;


import com.hth.mapper.UserMapper;
import com.hth.shiro.JWTToken;
import com.hth.entity.User;
import com.hth.mapper.UserMapper;
import com.hth.util.JWTUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 安全框架用的shiro 要重写doGetAuthenticationInfo方法
 * 添加我们自己的规则
 */

@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UserMapper userMapper;

    /**
     * 支持JWTToken
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 使用此方法进行id正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();
        Integer userId = JWTUtil.getUserId(token);
        if(userId==null)
        {
            throw new UnknownAccountException();
        }
        else{
            boolean verify = JWTUtil.verify(token, userId);
            if(!verify)
            {
                throw new UnknownAccountException();
            }
        }

        return new SimpleAuthenticationInfo(token, token,this.getName());
    }

    /**
     * 权限认证
     * 暂时还没有涉及到权限业务
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Integer userId = JWTUtil.getUserId(principals.toString());
        User user = userMapper.selectById(userId);
        if(user==null)
        {
            throw new UnknownAccountException();
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //如果用户名等于admin，则有admin Role
        if(user.getUsername().equals("admin")){
            info.addRole("admin");
        }
        return info;
    }


}
