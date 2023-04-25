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
import com.hth.mapper.UserMapper;
import com.hth.util.JWTUtil;
import io.swagger.models.auth.In;
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
    UserMapper userMapper;
    @Autowired
    PrivateChatService privateChatService;
    @Autowired
    PrivateMessageService privateMessageService;

    public JWTUtil jwtUtil = new JWTUtil();
    //获取聊天列表
    @GetMapping("/list")
    public Msg getChatList(){
        //获取用户Id
        int uid = jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        int all = 0;
        //获取所有数据。
        List<PrivateChat> list = privateChatService.list(uid,uid);
        //声明返回给前端
        List<ChatDto> res = new ArrayList<>(list.size());
        //设置有多少个未读
        for(PrivateChat chat:list){
            //查询未读
            int chatid = chat.getId();
            int count = privateMessageService.count(1,chatid,uid);
            all+=count;
            //查询对方的名字
            String name = "";
            if(chat.getUserOne()!=uid){
                name = userMapper.selectById(chat.getUserOne()).getUsername();
            }else{
                name = userMapper.selectById(chat.getUserTwo()).getUsername();
            }
            //封装ChatDto
            ChatDto chatDto = new ChatDto();
            chatDto.setPrivateChat(chat);
            chatDto.setUnread(count);
            chatDto.setToUsername(name);
            res.add(chatDto);
        }
        return Msg.success().add("list",res).add("all",all);
    }
    @GetMapping("/exists/{toId}")
    public Msg existsChat(@PathVariable Integer toId){
        int uid = jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        //先检索
        PrivateChat one = privateChatService.getone(uid,toId);
        //检查query1是否存在,存在则添加
        if(one!=null){
            return Msg.success().add("exist",true).add("chatId",one.getId());
        }
        //检索第二种情况
        PrivateChat two = privateChatService.getone(toId,uid);
        if(two!=null){
            return Msg.success().add("exist",true).add("chatId",two.getId());
        }
        return Msg.success().add("exist",false);
    }
    @PostMapping("/addChat/{toId}")
    public Msg addChat(@PathVariable Integer toId){
        //获取发送消息的用户
        int uid = jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        //都不存在则创建新的
        PrivateChat privateChat = new PrivateChat();
        privateChat.setUserOne(uid);//默认创建当前发起者为UserOne的chat
        privateChat.setUserTwo(toId);
        privateChatService.save(privateChat);
        return Msg.success("创建Chat成功").add("chatId",privateChat.getId());
    }
    //发送聊天消息
    @PostMapping("/add/{chatId}/{toId}")
    public Msg sendMessage(@RequestBody MessageBody messageBody,@PathVariable Integer chatId, @PathVariable Integer toId){
        //获取发送消息的用户
        int uid = jwtUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        PrivateChat chat = privateChatService.getById(chatId);
        addMessage(chat,uid,toId,messageBody.getMessage());
        return Msg.success("发送成功");


        //        //检查是否有与该用户的历史聊天记录，没有则添加新的，因为消息记录是用一个chatId来标识属于哪个chat，所以当不存在时，默认创建当前发起者为UserOne的chat。
//        //先检索
//        LambdaQueryWrapper<PrivateChat> query1 = new LambdaQueryWrapper<>();
//        query1.eq(PrivateChat::getUserOne,uid).and(x->x.eq(PrivateChat::getUserTwo,toId));
//        PrivateChat one = privateChatService.getOne(query1);
//        //检查query1是否存在,存在则添加
//        if(one!=null){
//            addMessage(one,uid,toId, messageBody.getMessage());
//            return Msg.success("发送成功");
//        }
//        //检索第二种情况
//        LambdaQueryWrapper<PrivateChat> query2 = new LambdaQueryWrapper<>();
//        query2.eq(PrivateChat::getUserOne,toId).and(x->x.eq(PrivateChat::getUserTwo,uid));
//        PrivateChat two = privateChatService.getOne(query2);
//        if(two!=null){
//            addMessage(two,uid,toId, messageBody.getMessage());
//            return Msg.success("发送成功");
//        }
    }

    //获取某个chat的所有消息
    @GetMapping("/messages/{chatId}/{toId}")
    public Msg getMessages(@PathVariable Integer chatId, @PathVariable Integer toId){
        Integer uid= JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        //获取所有的当前用户为sender的消息
        LambdaQueryWrapper<PrivateMessage> query1 = new LambdaQueryWrapper<>();
        query1.eq(PrivateMessage::getSenderId,uid).eq(PrivateMessage::getReceptorId,toId).eq(PrivateMessage::getChatId,chatId);
        query1.orderByAsc(PrivateMessage::getCreateTime);
        //获取当前用户为receptor的消息，并且全部设置为已读
        LambdaQueryWrapper<PrivateMessage> query2 = new LambdaQueryWrapper<>();
        query2.eq(PrivateMessage::getSenderId,toId).eq(PrivateMessage::getReceptorId,uid).eq(PrivateMessage::getChatId,chatId);
        query2.orderByAsc(PrivateMessage::getCreateTime);
        //先获取结果，在设置已读
        List<PrivateMessage> sendList = privateMessageService.list(query1);
        List<PrivateMessage> receptList = privateMessageService.list(query2);
        receptList.forEach(p->p.setState(0));
        privateMessageService.updateBatchById(receptList);
        sendList.addAll(receptList);
        sendList.sort((t1, t2) -> t1.getCreateTime().compareTo(t2.getCreateTime()));

        return Msg.success().add("list",sendList);
    }

    private void addMessage(PrivateChat chat,int fromId,int toId,String message){
        Date now = new Date();
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setCreateTime(now);
        privateMessage.setChatId(chat.getId());
        privateMessage.setSenderId(fromId);
        privateMessage.setReceptorId(toId);
        privateMessage.setContent(message);
        privateMessage.setState(1);//1代表未读
        privateMessageService.save(privateMessage);
        //再设置privateChat的最后一条信息以及时间
        chat.setLastMessage(message);
        chat.setLastTime(now);
        privateChatService.saveOrUpdate(chat);
        //chatServer推送
        try {
            chatServer.sendtoUser(message,toId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
