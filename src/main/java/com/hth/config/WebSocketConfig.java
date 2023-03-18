package com.hth.config;

import org.apache.catalina.session.StandardSessionFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator{
    private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
    /**
     * 注入ServerEndpointExporter，这个bean会自动注册使用了@ServerEndpoint注解声明的Websocket endpoint
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    /**
     * 修改握手信息
     */
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        StandardSessionFacade ssf = (StandardSessionFacade) request.getHttpSession();
        //添加用户的SessionId
        if (ssf != null) {
            HttpSession httpSession = (HttpSession) request.getHttpSession();
            //关键操作
            sec.getUserProperties().put("sessionId", httpSession.getId());
            log.info("获取到的SessionID：" + httpSession.getId());
        }
        super.modifyHandshake(sec, request, response);

    }

}