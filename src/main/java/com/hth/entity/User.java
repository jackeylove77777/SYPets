package com.hth.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2023-02-20 23:59:43
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user")
public class User  {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    
    private String username;
    
    private String password;
    
    private String salt;
    
    private String email;
    //头像
    private String avatar;
    //个人介绍
    private String privateInfo;
    
    private Integer sex;
    
    private Integer status;
    private Date createTime;

    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    @TableField(exist = false)
    private Profile profile;

}
