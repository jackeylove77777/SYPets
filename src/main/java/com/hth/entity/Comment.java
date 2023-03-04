package com.hth.entity;

import java.util.Date;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章的回答(Comment)表实体类
 *
 * @author makejava
 * @since 2023-02-20 23:58:48
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("comment")
public class Comment  {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    
    private Integer uid;
    
    private String uname;
    
    private String avatar;
    
    private Date time;
    
    private String content;
    //回复的帖子id
    private Integer pid;
    
    private Integer status;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;
    private List<Answer> answerList;//记录这条评论的所有回复


}
