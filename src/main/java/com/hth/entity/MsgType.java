package com.hth.entity;


import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 推送消息的类型,点赞、关注、评论、回复(MsgType)表实体类
 *
 * @author makejava
 * @since 2023-02-20 23:59:16
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("msg_type")
public class MsgType  {
    @TableId
    private Integer id;

    
    private String name;



}
