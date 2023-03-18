package com.hth.chat;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hth.chat.entity.ChatDto;
import com.hth.chat.entity.MessageBody;
import com.hth.chat.entity.PrivateChat;
import com.hth.chat.entity.PrivateMessage;
import com.hth.chat.service.PrivateChatService;
import com.hth.chat.service.PrivateMessageService;
import com.hth.entity.Msg;
import com.hth.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    为了实现简单的私聊系统，不考虑分页分段获取数据了，一次性加载所有
 */
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    ChatServer chatServer;

    @Autowired
    PrivateChatService privateChatService;
    @Autowired
    PrivateMessageService privateMessageService;
    //获取聊天列表
    @GetMapping("/list/{id}")
    public Msg getChatList(@PathVariable Integer id){
        LambdaQueryWrapper<PrivateChat> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PrivateChat::getUserOne,id);
        List<PrivateChat> list = privateChatService.list(queryWrapper);
        List<ChatDto> res = new ArrayList<>(list.size());
        //设置有多少个未读
        for(PrivateChat chat:list){
            //查询未读
            LambdaQueryWrapper<PrivateMessage> messageQuery = new LambdaQueryWrapper<>();
            messageQuery.eq(PrivateMessage::getState,1).eq(PrivateMessage::getSenderId,chat.getUserTwo()).eq(PrivateMessage::getReceptorId,id);
            int count = privateMessageService.count(messageQuery);
            ChatDto chatDto = new ChatDto();
            chatDto.setPrivateChat(chat);
            chatDto.setUnread(count);
            res.add(chatDto);
        }
        return Msg.success().add("charList",privateChatService.list(queryWrapper));
    }

    //发送聊天消息
    @PostMapping("/add/{toId}")
    public Msg sendMessage(@RequestBody MessageBody messageBody,@PathVariable Integer toId){
        int uid = messageBody.getFromId();
        //检查是否有与该用户的历史聊天记录，没有则添加新的
        LambdaQueryWrapper<PrivateChat> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PrivateChat::getUserOne,messageBody.getFromId()).eq(PrivateChat::getUserTwo,toId);
        PrivateChat one = privateChatService.getOne(queryWrapper);
        if(one==null){
            PrivateChat user1 = new PrivateChat();
            user1.setUserOne(uid);
            user1.setUserTwo(toId);
            PrivateChat user2 = new PrivateChat();
            user2.setUserOne(toId);
            user2.setUserTwo(uid);
            privateChatService.save(user1);
            privateChatService.save(user2);
        }
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setCreateTime(new Date());
        privateMessage.setSenderId(uid);
        privateMessage.setReceptorId(toId);
        privateMessage.setContent(messageBody.getMessage());
        privateMessageService.save(privateMessage);
        String message = JSON.toJSONString(messageBody);
        boolean ok = false;
        try {
            ok = chatServer.sendtoUser(message,toId);
        } catch (IOException e) {
            return Msg.fail("发送失败");
        }
        if(ok){
            return Msg.success("发送成功");
        }else{
            return Msg.fail("发送失败");
        }
    }

    //获取某个chat的所有消息
    @GetMapping("/messages/{toId}")
    public Msg getMessages(@PathVariable Integer toId){
        Integer uid= JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        //获取所有的当前用户为sender的消息
        LambdaQueryWrapper<PrivateMessage> query1 = new LambdaQueryWrapper<>();
        query1.eq(PrivateMessage::getSenderId,uid).eq(PrivateMessage::getReceptorId,toId);
        //获取当前用户为receptor的消息，并且全部设置为已读
        LambdaQueryWrapper<PrivateMessage> query2 = new LambdaQueryWrapper<>();
        query2.eq(PrivateMessage::getSenderId,toId).eq(PrivateMessage::getReceptorId,uid);

        List<PrivateMessage> sendList = privateMessageService.list(query1);
        List<PrivateMessage> receptList = privateMessageService.list(query2);

        receptList.forEach(p->p.setState(0));
        return Msg.success().add("sendList",sendList).add("receptList",receptList);
    }
}
