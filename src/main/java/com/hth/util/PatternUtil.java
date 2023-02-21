package com.hth.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {
    /**
     * 将字符串带@* 变成 <a href='@*'></a> 帮助我们完全@功能
     * @param s
     * @return
     */
    public static String replace(String s, Set<String> userNameSet){
        Pattern pattern= Pattern.compile("@[\\w,[\\u4E00-\\u9FA5]]+");
        Matcher matcher=pattern.matcher(s);
        Set<String> set=new HashSet<>();
        while (matcher.find()){
            String str=matcher.group();
            Pattern patterns=Pattern.compile(str);
            Matcher matcher1=patterns.matcher(s);
            if(userNameSet.contains(str.substring(1))&&!set.contains(str.substring(1)))
            {
                s=matcher1.replaceAll("[@"+str.substring(1)+"]"+"(/profile/"+str.substring(1)+")");
                set.add(str.substring(1));
            }
        }
        return  s;
    }

    public static Set<String> findName(String s) {
        Set<String> usernameSet=new HashSet<>();
//        Pattern pattern=Pattern.compile("@\\w+");
        Pattern pattern=Pattern.compile("@[\\w,[\\u4E00-\\u9FA5]]+");
        Matcher matcher=pattern.matcher(s);
        while (matcher.find()){
            usernameSet.add(matcher.group().substring(1));
        }
        return usernameSet;
    }

}