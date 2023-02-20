package com.hth.entity;


import java.io.Serializable;
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
    @TableId
    private Integer id;

    
    private String name;
    
    private String description;



}
