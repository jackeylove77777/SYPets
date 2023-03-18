package com.hth.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.chat.entity.PrivateChat;
import com.hth.chat.service.PrivateChatService;
import com.hth.mapper.PrivateChatMapper;
import org.springframework.stereotype.Service;

/**
 * 私信列表的表，用于记录当前用户与谁有聊天记录(PrivateChat)表服务实现类
 *
 * @author makejava
 * @since 2023-03-11 19:42:09
 */
@Service
public class PrivateChatServiceImpl extends ServiceImpl<PrivateChatMapper, PrivateChat> implements PrivateChatService {

}
