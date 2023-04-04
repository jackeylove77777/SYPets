package com.hth.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Foster)表实体类
 *
 * @author makejava
 * @since 2023-03-10 23:34:12
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("foster")
public class Foster  {
    @TableId
    private Integer id;

    
    private String description;
    //发布的用户
    private Integer uid;
    
    private Date publishTime;
    //照片
    private String image;
    //地点
    private String address;
    //联系方式
    private String contaction;
    //0为未审核
    private Integer status;
    //0代表未给宠物找到新主人
    private Integer solve;
    
    private Integer delFlag;
    @TableField(exist = false)
    private String expectTime;


}
