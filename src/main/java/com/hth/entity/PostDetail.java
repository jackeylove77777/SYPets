package com.hth.entity;

import lombok.Data;

@Data
public class PostDetail extends Post  {

    //类型名称
    private String name;
    //文章的作者名称
    private String username;

    private String avatar;
    //点赞数
    private Integer starts;
    //收藏数
    private Integer collects;
    //登录用户是否点赞了
    private boolean isStart;
    //登录用户是否收藏了
    private boolean isCollect;

}