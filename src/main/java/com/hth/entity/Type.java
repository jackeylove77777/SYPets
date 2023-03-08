package com.hth.entity;


import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 文章的类型(Type)表实体类
 *
 * @author makejava
 * @since 2023-02-20 23:59:35
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("type")
public class Type  {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    
    private String name;
    
    private String description;
    @TableField(exist = false)
    private Integer type_num;

}
