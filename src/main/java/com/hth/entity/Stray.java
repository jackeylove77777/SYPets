package com.hth.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Stray)表实体类
 *
 * @author makejava
 * @since 2023-03-10 23:35:53
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("stray")
public class Stray  {
    @TableId
    private Integer id;

    
    private String description;
    //发布的用户
    private Integer uid;
    
    private Date publishTime;
    //照片
    private String image;
    //发现地点
    private String address;
    //0为未审核
    private Integer status;
    
    private Integer delFlag;



}
