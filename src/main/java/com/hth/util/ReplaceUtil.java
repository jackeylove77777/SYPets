package com.hth.util;


/**
 * 替换工具类
 */
public class ReplaceUtil {
    /**
     * 用户名替换成 个人主页的link
     * @param username
     * @return
     */
    public static String userReplace(String username){
       return  "<a "+"href='/profile/"+username+"'>"+username+"</a>";
    }

    /**
     * 通过文章id 替换成文章详情的页面的link
     * @param id
     * @param title
     * @return
     */
    public static String PostReplace(int id,String title){
        return "<a "+"href='/detail/"+id+"'>"+title+"</a>";
    }

    /**
     * 通过被提到的id，替换成被提到的具体内容的link
     * @param id
     * @param title
     * @return
     */
    public static String mentionReplace(int id,String title){
        return "<a "+"href='/mention/"+id+"'>"+title+"</a>";
    }
}
