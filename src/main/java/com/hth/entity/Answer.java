package com.hth.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论的回复(Answer)表实体类
 *
 * @author makejava
 * @since 2023-02-20 23:58:28
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("answer")
public class Answer  {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    
    private Integer uid;
    
    private String uname;
    
    private String avatar;
    
    private String content;
    
    private Date time;
    //回复的评论id
    private Integer commentId;
    
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}
