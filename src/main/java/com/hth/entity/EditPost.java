package com.hth.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (EditPost)表实体类
 *
 * @author makejava
 * @since 2023-02-23 23:11:33
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("edit_post")
public class EditPost  {

    
    private Integer id;
    
    private String content;


}
