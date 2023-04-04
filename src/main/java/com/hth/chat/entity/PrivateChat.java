package com.hth.chat.entity;


import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * 私信列表的表，用于记录当前用户与谁有聊天记录(PrivateChat)表实体类
 *
 * @author makejava
 * @since 2023-03-11 19:42:08
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("private_chat")
public class PrivateChat  {
    @TableId
    private Integer id;

    
    private Integer userOne;
    
    private Integer userTwo;
    
    private String lastMessage;

    private Date lastTime;

}
