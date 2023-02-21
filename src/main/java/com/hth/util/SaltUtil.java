package com.hth.util;

import java.util.Random;

public class SaltUtil {
    /**
     * 注册登录都是通过md5+salt的方式
     * 这是函数是用来生产随机salt、随机的验证码（邮箱验证码）
     * @param n
     * @return
     */
    public static String getSalt(int n){
        String s="0123456789";
        for(char a='A';a<='Z';a++)
            s+=a;
        for(char a='a';a<='z';a++)
            s+=a;
        String res="";
        for(int i=0;i<n;i++){
            res+=s.charAt(new Random().nextInt(s.length()-1));
        }
        return res;
    }

}