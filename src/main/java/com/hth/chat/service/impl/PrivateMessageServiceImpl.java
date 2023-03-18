package com.hth.chat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.chat.entity.PrivateMessage;
import com.hth.chat.service.PrivateMessageService;
import com.hth.mapper.PrivateMessageMapper;
import org.springframework.stereotype.Service;

/**
 * (PrivateMessage)表服务实现类
 *
 * @author makejava
 * @since 2023-03-11 19:42:42
 */
@Service
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage> implements PrivateMessageService {

}
