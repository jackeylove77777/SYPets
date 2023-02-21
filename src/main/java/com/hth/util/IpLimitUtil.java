package com.hth.util;

import com.hth.entity.LimitIp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * ip限流工具类，限制邮件或者接口被访问的频率
 */
@Component
public class IpLimitUtil {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     *  ip限流工具，优先把时间久的加入
     */
    public Map<String,String> ipContro(List<LimitIp> list){
        Map<String,String> map=new HashMap<>();
        for(LimitIp ip:list){
            int count=redisTemplate.opsForValue().increment(ip.getKey()).intValue();
            if(count==1){
                redisTemplate.expire(ip.getKey(),ip.getSec(),TimeUnit.SECONDS);//设置过期时间
            }
            if(count>ip.getCount()){
                map.put("message","访问超出限制,请"+ip.getTime()+"再来访问");
                return map;
            }
        }
        map.put("message","success");
        return map;
    }
}
