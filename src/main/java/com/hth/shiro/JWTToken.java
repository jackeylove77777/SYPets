package com.hth.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @description  对token进行扩展
 */
public class JWTToken implements AuthenticationToken {
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }
    //获取账号
    @Override
    public Object getPrincipal() {
        return token;
    }
    //获取密码
    @Override
    public Object getCredentials() {
        return token;
    }
}
