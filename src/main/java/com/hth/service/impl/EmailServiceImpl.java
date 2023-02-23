package com.hth.service.impl;

import com.hth.service.EmailService;
import com.hth.util.SaltUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

@Component
@Service
public class EmailServiceImpl implements EmailService {
    @Lazy
    @Autowired
    RedisTemplate redisTemplate;
    @Lazy
    @Autowired
    JavaMailSenderImpl mailSender;

    /**
     * 发送注册验证码，因为十分耗时，采用了异步任务@Async
     * @param toEmail
     */
    @Async("mySimpleAsync")
    public void sendRegisterEmail(String toEmail) {
        //随机生成6位数
        String code = SaltUtil.getSalt(6);
//        emailUtil.sendEmai("注册验证码", "<html><body>你在<a href='http://localhost:8080/'>微社区</a>进行注册" +
//                "验证码为<h2>" + code + "<h2><br>请在30分钟内完成注册<body></html>", toEmail);
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper= null;
        try {
            helper = new MimeMessageHelper(message,true);
            helper.setSubject("注册验证码");
            helper.setText(
                    "<html><body>你在<a href='http://localhost:8080/'>思远宠物论坛</a>进行注册"+
                            "验证码为<h2>"+code+"<h2><br>请在30分钟内完成注册<body></html>", true);
            helper.setTo(toEmail);
            helper.setFrom("3579949927@qq.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(message);
        //设置验证码的有效时间
        redisTemplate.opsForValue().set(toEmail + ":code", code, 60 * 30, TimeUnit.SECONDS);
    }

    /**
     * 跟上面类似，这个是重置密码
     * @param email
     * @param psw
     */
    @Async("mySimpleAsync")
    public void sendPsw(String email,String psw)  {
        MimeMessage message=mailSender.createMimeMessage();
        MimeMessageHelper helper= null;
        try {
            helper = new MimeMessageHelper(message,true);
            helper.setSubject("重置密码");
            helper.setText(
                    "<html><body>你在<a href='http://localhost:8080/'>思远宠物论坛</a>进行重置密码"+
                            "新密码为<h2>"+psw+"<h2><br>建议尽快登录修改密码<body></html>", true);
            helper.setTo(email);
            helper.setFrom("3579949927@qq.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(message);

    }

}
