package com.hth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hth.constants.SystemConstants;
import com.hth.entity.*;
import com.hth.mapper.*;
import com.hth.service.MessageService;
import com.hth.util.JWTUtil;
import com.hth.util.ReplaceUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    @Autowired
    MessageMapper messageMapper;
    @Autowired
    MsgTypeMapper msgTypeMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    PostMapper postMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;
    /**
     * 评论消息提醒 异步任务
     * @param id 评论者的ID
     * @param message 内容
     * @param type_id 消息类型
     */
    @Async("mySimpleAsync")
    public void addMessage(Integer id,String message,Integer type_id) {
        Message msg=new Message();
        msg.setToId(id);
        msg.setMessage(message);
        msg.setTypeId(type_id);
        msg.setTime(new Date());
        rabbitTemplate.convertAndSend("fanout_msg_exchange","",String.valueOf(id));
        messageMapper.insert(msg);

    }

    /**
     * 回复消息提醒
     * @param type_id 消息类型
     * @param commentId 评论id
     * @param username 哪个用户
     */
    @Async("mySimpleAsync")
    public void addMessage(Integer type_id,Integer commentId,String username){
        Comment comment = commentMapper.selectById(commentId);//被回复的评论
        Post post = postMapper.selectById(comment.getPid());//该回复所在的文章
        Message msg=new Message();
        msg.setTypeId(type_id);
        msg.setToId(comment.getUid());
        msg.setTime(new Date());
        String message= ReplaceUtil.userReplace(username)+"在"+ReplaceUtil.PostReplace(post.getId(),post.getTitle())+"回复了你";
        msg.setMessage(message);
        rabbitTemplate.convertAndSend("fanout_msg_exchange","",String.valueOf(msg.getToId()));
        messageMapper.insert(msg);
    }

    /**
     * 点赞消息提醒
     * @param uid 用户id
     * @param pid 文章id
     * @param typeId 消息类型
     */
    @Async("mySimpleAsync")
    public void addStartMessage(Integer uid,Integer pid,Integer typeId){
        User user=userMapper.selectById(uid);//点赞人的id
        Post post=postMapper.selectById(pid);//被点赞文章的作者
        String msg=ReplaceUtil.userReplace(user.getUsername())+"给你的文章"+ReplaceUtil.PostReplace(post.getId(),post.getTitle())+
                "点赞了";
        Message message=new Message();
        message.setTime(new Date());
        message.setTypeId(typeId);
        message.setToId(post.getUid());
        message.setMessage(msg);
        rabbitTemplate.convertAndSend("fanout_msg_exchange","",String.valueOf(message.getToId()));
        messageMapper.insert(message);
    }

    /**
     * 关注消息的推送
     * from_id 关注了 to_id
     * @param from_id
     * @param to_id
     * @param typeId 消息类型
     */
    @Override
    public void addFollow(Integer from_id, Integer to_id,Integer typeId) {
        Message msg=new Message();
        msg.setToId(to_id);
        msg.setTypeId(typeId);
        msg.setTime(new Date());
        String username=userMapper.selectById(from_id).getUsername();
        String message=ReplaceUtil.userReplace(username)+"关注了你";
        msg.setMessage(message);
        rabbitTemplate.convertAndSend("fanout_msg_exchange","",String.valueOf(to_id));
        messageMapper.insert(msg);
    }

    /**
     * 当前登录用户
     * 是否有消息未读的 status为1则为未读
     * @return
     */
    @Override
    public boolean exitMessage() {
        int uid= JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getToId,uid);
        queryWrapper.eq(Message::getStatus, SystemConstants.MSG_NOT_READ);
        Integer messageCount = messageMapper.selectCount(queryWrapper);
        return messageCount>0;
    }

    /**
     * 返回所有的消息类型
     * @return
     */
    @Override
    public List<MessageType> findMessageTypeNum() {
        int uid=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        LambdaQueryWrapper<MessageType> queryWrapper = new LambdaQueryWrapper<>();
        List<MessageType> messageTypeNum = msgTypeMapper.selectList(queryWrapper);
        messageTypeNum.forEach(m->m.setValue(uid));
        return messageTypeNum;
    }

    /**
     * 反回某一类型的所有消息
     * @param typeId 类型ID
     * @return
     */
    @Override
    public List<Message> findMessageByTypeId(Integer typeId) {
        int uid=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getTypeId,typeId);
        queryWrapper.eq(Message::getToId,uid);
        List<Message> messageList = messageMapper.selectList(queryWrapper);
        messageList.sort((o1, o2) -> {
            if(o1.getStatus()>o2.getStatus())//未读优先
                return 1;
            return (int) (o2.getTime().getTime()-o1.getTime().getTime());
        });
        return messageList;
    }

    /**
     * 修改消息的状态
     * @param id  消息的id
     * @param status 状态
     * @return
     */
    @Override
    public boolean update(Integer id,Integer status) {
        Integer uid=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        LambdaQueryWrapper<Message> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Message::getId,id);
        queryWrapper.eq(Message::getToId,uid);
        Message message = messageMapper.selectOne(queryWrapper);
        if(message!=null){
            message.setStatus(status);
            messageMapper.updateById(message);
            return true;
        }
        return false;
    }
}
