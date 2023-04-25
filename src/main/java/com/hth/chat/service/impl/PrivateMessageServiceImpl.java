package com.hth.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.chat.entity.MessageBody;
import com.hth.chat.entity.PrivateMessage;
import com.hth.chat.service.PrivateMessageService;
import com.hth.mapper.PrivateMessageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (PrivateMessage)表服务实现类
 *
 * @author makejava
 * @since 2023-03-11 19:42:42
 */
@Service
public class PrivateMessageServiceImpl extends ServiceImpl<PrivateMessageMapper, PrivateMessage> implements PrivateMessageService {
    @Autowired
    PrivateMessageMapper privateMessageMapper;

    @Override
    public int count(int status, int chatid, int uid) {
        LambdaQueryWrapper<PrivateMessage> messageQuery = new LambdaQueryWrapper<>();
        //只需要查询Receptor即可,先是找出状态未1代表未读，再是chatId与当前循环的chat的id相等的，最后是
        messageQuery.eq(PrivateMessage::getState,1).eq(PrivateMessage::getChatId,chatid).eq(PrivateMessage::getReceptorId,uid);
        return privateMessageMapper.selectCount(messageQuery);
    }

    @Override
    public boolean save(PrivateMessage privateMessage) {
        int rows = privateMessageMapper.insert(privateMessage);
        return rows>0;
    }
}
