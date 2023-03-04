package com.hth.service.impl;

import com.hth.entity.Profile;
import com.hth.service.InteractionService;
import com.hth.service.MessageService;
import com.hth.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
public class InteractionServiceImpl implements InteractionService {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    MessageService messageService;


    /**
     * 给文章点赞
     * @param postId 文章id
     * @return
     */
    @Override
    public int onStart(Integer postId) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        redisTemplate.opsForSet().add("post:" + postId + ":start",userId);
        //发送点赞消息
        messageService.addStartMessage(userId,postId,4);
        return 1;
    }

    /**
     * 取消点赞
     * @param postId 文章id
     * @return
     */
    @Override
    public int unStart(Integer postId) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        redisTemplate.opsForSet().remove("post:" + postId + ":start",userId);
        return 1;
    }
    /**
     * 是否已经点赞了
     * @param postId 文章id
     * @return
     */
    @Override
    public boolean existsStar(Integer postId) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        return redisTemplate.opsForSet().isMember("post:" + postId + ":start",userId).booleanValue();
    }

    /**
     * 收藏文章
     * @param postId 文章id
     * @return
     */
    @Override
    public int onCollect(Integer postId) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        //用户添加对文章的收藏
        redisTemplate.opsForZSet().add(userId+":collect",postId,new Date().getTime());
        //收餐文章的用户们
        redisTemplate.opsForSet().add("post:" + postId + ":collect",userId);
        return 1;
    }
    /**
     * 是否已经收藏文章
     * @param postId 文章ID
     * @return
     */
    @Override
    public boolean existsCollect(Integer postId) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        return redisTemplate.opsForSet().isMember("post:" + postId + ":collect",userId);
    }

    /**
     * 取消收藏
     * @param postId
     * @return
     */
    @Override
    public int unCollect(Integer postId) {
        Integer userId=JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        redisTemplate.opsForZSet().remove(userId+":collect",postId);
        redisTemplate.opsForSet().remove("post:" + postId + ":collect",userId);
        return 0;
    }

    /**
     * 通过用户的id  找出他的粉丝、关注、收藏数
     * @param userId
     * @return
     */
    @Override
    public Profile findUserProfile(Integer userId) {
        Profile profile=new Profile();

        profile.setCollects(redisTemplate.opsForZSet().size(userId+":collect").intValue());

        profile.setFollowers(redisTemplate.opsForSet().size(userId+":followers").intValue());

        profile.setFollowings(redisTemplate.opsForSet().size(userId+":followings").intValue());
        return profile;
    }
}