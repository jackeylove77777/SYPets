package com.hth.chat.entity;

import java.util.Date;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * (PrivateMessage)表实体类
 *
 * @author makejava
 * @since 2023-03-11 20:23:47
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("private_message")
public class PrivateMessage  {
    @TableId
    private Integer id;

    
    private Integer chatId;
    //是否已读,1代表未读
    private Integer state;
    //发送者Id
    private Integer senderId;
    //接收者Id
    private Integer receptorId;
    
    private Date createTime;
    
    private String content;



}
