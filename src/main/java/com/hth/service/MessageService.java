package com.hth.service;

import com.hth.entity.Message;
import com.hth.entity.MessageType;

import java.util.List;

public interface MessageService {
    void addMessage(Integer id,String message,Integer type_id);

    void addMessage(Integer type_id,Integer commentId,String username);

    void addStartMessage(Integer uid,Integer pid,Integer typeId);

    void addFollow(Integer from_id,Integer to_id,Integer typeId);

    boolean exitMessage();

    List<MessageType> findMessageTypeNum();

    List<Message> findMessageByTypeId(Integer id);

    boolean update(Integer id,Integer status);
}