package com.hth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class LimitIp implements Serializable {
    //设置过期时间的key
    private String key;
    //要设置的过期时间
    private Integer sec;
    //访问次数
    private Integer count;
    //提示多久可以访问;
    private String time;
}
