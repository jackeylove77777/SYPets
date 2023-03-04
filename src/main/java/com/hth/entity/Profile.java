package com.hth.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Profile implements Serializable {
    private Integer followers;//粉丝数
    private Integer followings;//关注数量
    private Integer collects;//收藏数量
}