package com.hth.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 友链(Advertisement)表实体类
 *
 * @author makejava
 * @since 2023-03-08 11:07:19
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("advertisement")
public class Advertisement  {
    @TableId
    private Long id;

    
    private String name;
    
    private String avatar;
    
    private String description;
    //网站地址
    private String address;
    //审核状态 (0代表审核通过，1代表审核未通过，2代表未审核)
    private String status;
    
    private Long createBy;

    private Date createTime;
    //删除标志（0代表未删除，1代表已删除）
    private Integer delFlag;



}
