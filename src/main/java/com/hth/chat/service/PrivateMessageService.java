package com.hth.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hth.chat.entity.MessageBody;
import com.hth.chat.entity.PrivateMessage;


/**
 * (PrivateMessage)表服务接口
 *
 * @author makejava
 * @since 2023-03-11 20:23:47
 */
public interface PrivateMessageService extends IService<PrivateMessage> {
    int count(int status,int chatid,int uid);
    boolean save(PrivateMessage privateMessage);
}
