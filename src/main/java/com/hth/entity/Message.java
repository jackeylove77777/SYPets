package com.hth.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 用户之间的消息表(Message)表实体类
 *
 * @author makejava
 * @since 2023-02-20 23:59:08
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("message")
public class Message  {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    
    private Integer toId;
    
    private String message;
    
    private Date time;
    
    private Integer typeId;
    //代表是否被读
    private Integer status;
    
    private Date createTime;
    
    private Date updateTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}
