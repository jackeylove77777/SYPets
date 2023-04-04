package com.hth.chat;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.*;

import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.hth.config.WebSocketConfig;
import com.hth.util.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@ServerEndpoint(value = "/socket", configurator = WebSocketConfig.class)
@Component
public class ChatServer {

    static Logger log = LoggerFactory.getLogger(ChatServer.class);
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    private static ConcurrentHashMap<Integer, ChatServer> hashmap = new ConcurrentHashMap<>();
    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;
    private Integer userId;
    private Integer getUserId(Session session){
        //请求为   ws://domain?token=jwt,token存了userid
        String token= session.getQueryString().split("=")[1];
        Integer userId = JWTUtil.getUserId(token);
        return userId;
    }
    @OnOpen
    public void onOpen(Session session) {
        this.session=session;
        this.userId=getUserId(session);
        hashmap.put(this.userId,this);
        log.info("【websocket消息】新建连接, 总数:{}",hashmap.size());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        hashmap.remove(userId);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + userId + "的信息:" + message);
        //群发消息
        //do nothing
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    public  boolean sendtoUser(String message,Integer sendUserId) throws IOException{
        if (hashmap.get(sendUserId) == null) {
            log.info("未找到匹配用戶");
            return false;
        }else{
            hashmap.get(sendUserId).sendMessage(message);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("消息发送成功");
            return true;
        }
    }


    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        ChatServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        ChatServer.onlineCount--;
    }
}

