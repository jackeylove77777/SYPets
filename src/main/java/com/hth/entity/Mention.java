package com.hth.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息推送:例如，点赞、关注、评论、回复评论这些功能(Mention)表实体类
 *
 * @author makejava
 * @since 2023-02-20 23:58:56
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mention")
public class Mention  {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    
    private Integer fromId;
    
    private Integer toId;
    
    private String title;
    
    private String description;
    
    private String content;
    
    private Date time;
    
    private Integer status;
    
    private Integer typeId;
    
    private Integer pid;
    private Date createTime;
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}
