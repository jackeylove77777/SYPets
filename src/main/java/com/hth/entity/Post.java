package com.hth.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章帖子表(Post)表实体类
 *
 * @author makejava
 * @since 2023-02-20 23:59:25
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("post")
public class Post  {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    
    private String title;
    
    private String description;
    
    private String content;
    
    private Integer uid;
    
    private Integer status;
    
    private Integer typeId;
    
    private Integer author;

    private Date createTime;
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}
