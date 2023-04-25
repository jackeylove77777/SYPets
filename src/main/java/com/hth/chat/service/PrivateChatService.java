package com.hth.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hth.chat.entity.PrivateChat;
import com.hth.mapper.PrivateChatMapper;

import java.util.List;


/**
 * 私信列表的表，用于记录当前用户与谁有聊天记录(PrivateChat)表服务接口
 *
 * @author makejava
 * @since 2023-03-11 19:42:08
 */
public interface PrivateChatService extends IService<PrivateChat> {
    List<PrivateChat> list(int one,int two);
    PrivateChat getone(int one,int two);
    boolean save(PrivateChat chat);
    PrivateChat getById(int chatid);
}
