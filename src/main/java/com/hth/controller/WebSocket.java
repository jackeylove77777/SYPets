package com.hth.controller;

import com.hth.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
@Component
@ServerEndpoint(value = "/websocket")
@Slf4j
public class WebSocket {
    private static ConcurrentHashMap<Integer,WebSocket> hashMap=new ConcurrentHashMap<>();
    private Session session;
    private Integer userId;
    private Integer getUserId(Session session){
        //请求为   ws://xxxxxxx?token=jwt,jwttoken存了userid
        String token= session.getQueryString().split("=")[1];
        Integer userId = JWTUtil.getUserId(token);
        return userId;
    }
    @OnOpen
    public void onOpen(Session session) {
        this.session=session;
        this.userId=getUserId(session);
        hashMap.put(this.userId,this);
//        log.info(""+this.userId);
        log.info("【websocket消息】新建连接, 总数:{}",hashMap.size());
    }


    @OnClose
    public void onClose() {
        hashMap.remove(userId);
        log.info("【websocket消息】连接断开, 总数:{}",hashMap.size());
    }

    @OnMessage
    public void onMessage(String message) {
        log.info("【websocket消息】收到客户端发来的消息:{}", message);
    }

    /**
     * 点对点广播消息
     * InteractionService生产消息，并发送到msg.fanout.queue，此方法监听此队列，当发现有新的消息产生，则对相应的用户进行提示
     * @param userId 要广播的用户id
     * @param msg    消息
     */
    @RabbitListener(queues = "msg.fanout.queue")
    public void  receiveMessage(String msg) throws IOException {
        log.info("收到消息："+msg);
        sendP2PMessage(Integer.parseInt(msg));
    }
    public void sendP2PMessage(Integer userId) throws IOException {
        WebSocket socket = hashMap.getOrDefault(userId, null);
        if(socket!=null){
            log.info("发送消息:"+userId);
            socket.session.getBasicRemote().sendText("有新的通知");
        }
    }

}