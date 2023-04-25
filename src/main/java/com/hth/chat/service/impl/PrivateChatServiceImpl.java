package com.hth.chat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.chat.entity.PrivateChat;
import com.hth.chat.service.PrivateChatService;
import com.hth.mapper.PrivateChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 私信列表的表，用于记录当前用户与谁有聊天记录(PrivateChat)表服务实现类
 *
 * @author makejava
 * @since 2023-03-11 19:42:09
 */
@Service
public class PrivateChatServiceImpl extends ServiceImpl<PrivateChatMapper, PrivateChat> implements PrivateChatService {
    @Autowired
    PrivateChatMapper privateChatMapper;

    @Override
    public List<PrivateChat> list(int one, int two) {
        LambdaQueryWrapper<PrivateChat> queryWrapper = new LambdaQueryWrapper<>();
        //发起者或者接收者有用户则都要获取这个聊天列表项
        queryWrapper.eq(PrivateChat::getUserOne,one).or(x->x.eq(PrivateChat::getUserTwo,two));
        return privateChatMapper.selectList(queryWrapper);
    }

    @Override
    public PrivateChat getone(int one, int two) {
        LambdaQueryWrapper<PrivateChat> query1 = new LambdaQueryWrapper<>();
        query1.eq(PrivateChat::getUserOne,one).and(x->x.eq(PrivateChat::getUserTwo,two));
        return privateChatMapper.selectOne(query1);
    }

    @Override
    public boolean save(PrivateChat chat) {
        int rows = privateChatMapper.insert(chat);
        return rows>0;
    }

    @Override
    public PrivateChat getById(int chatid) {
        return privateChatMapper.selectById(chatid);
    }
}
