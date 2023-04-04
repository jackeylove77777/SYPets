package com.hth.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (Findpet)表实体类
 *
 * @author makejava
 * @since 2023-04-02 00:10:52
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("findpet")
public class Findpet  {
    @TableId
    private Integer id;

    
    private String description;
    //发布的用户
    private Integer uid;
    
    private Date publishTime;
    
    private Date lostTime;
    //照片
    private String image;
    //地点
    private String address;
    //联系方式
    private String contaction;
    //0为未审核
    private Integer status;
    //0代表未找到该宠物
    private Integer solve;
    
    private Integer delFlag;



}
