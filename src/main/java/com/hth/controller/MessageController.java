package com.hth.controller;

import com.hth.entity.Message;
import com.hth.entity.MessageType;
import com.hth.entity.Msg;
import com.hth.log.Logweb;
import com.hth.log.TestSuccess;
import com.hth.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageService messageService;

    /**
     * 查询是否有消息未查看的
     * @return
     */
    @GetMapping("/exists")
    public Msg exists(){
        return Msg.success().add("exists",messageService.exitMessage());
    }
    /**
     * 返回当前用户未阅读的消息的所有类型及其数量
     * @return
     */
    @GetMapping("/findMessageTypeNum")
    @TestSuccess
    public Msg findMessageTypeNum(){
        List<MessageType> messageTypeNum = messageService.findMessageTypeNum();
        return Msg.success().add("data",messageTypeNum);
    }

    /**
     * 查出某一类型的消息
     * @param id
     * @return
     */
    @GetMapping("/findMessageById/{id}")
    @TestSuccess
    public Msg findMessageById(@PathVariable Integer id){
        List<Message> messageList = messageService.findMessageByTypeId(id);
        System.out.println(messageList);
        return Msg.success().add("list",messageList);
    }

    /**
     * 消息标记为已读  1代表已读
     * @param id 消息的id
     * @return
     */
    @Logweb("已读消息")
    @GetMapping("/read/{id}")
    @TestSuccess
    public Msg read(@PathVariable Integer id){
        boolean up = messageService.update(id,2);
        return up?Msg.success("success"):Msg.fail("error");
    }
    /**
     * 删除消息    0代表删除
     * @param id
     * @return
     */
    @Logweb("删除消息")
    @GetMapping("/delete/{id}")
    @TestSuccess
    public Msg delete(@PathVariable Integer id){
        boolean up = messageService.update(id,0);
        return up?Msg.success("success"):Msg.fail("error");
    }
}
