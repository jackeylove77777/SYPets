package com.hth.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
public class MessageType {
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    
    private String name;
    @TableField(exist = false)
    private Integer value;

}
