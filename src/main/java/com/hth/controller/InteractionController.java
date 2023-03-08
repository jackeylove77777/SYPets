package com.hth.controller;

import com.hth.entity.Msg;
import com.hth.log.TestSuccess;
import com.hth.service.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@TestSuccess
public class InteractionController {
    @Autowired
    InteractionService interactionService;
    //点赞
    @GetMapping("/onStart/{postId}")
    @TestSuccess
    public Msg onStart(@PathVariable Integer postId){
        if(interactionService.existsStar(postId)){
            return Msg.fail("点赞失败");
        }
        interactionService.onStart(postId);
        return Msg.success("点赞成功");
    }
    //取消点赞
    @GetMapping("/unStart/{postId}")
    @TestSuccess
    public Msg unStart(@PathVariable Integer postId){
        if(!interactionService.existsStar(postId)){
            return Msg.fail("取消点赞失败");
        }
        interactionService.unStart(postId);
        return Msg.success("取消点赞成功");
    }
    //收藏
    @GetMapping("/onCollect/{postId}")
    @TestSuccess
    public Msg onCollect(@PathVariable Integer postId){
        if(interactionService.existsCollect(postId)){
            return Msg.fail("收藏失败");
        }
        else {
            interactionService.onCollect(postId);
        }
        return Msg.success("收藏成功");
    }

    //取消收藏
    @GetMapping("/unCollect/{postId}")
    @TestSuccess
    public Msg unCollect(@PathVariable Integer postId){
        if(!interactionService.existsCollect(postId)){
            return Msg.fail("取消收藏失败");
        }
        else {
            interactionService.unCollect(postId);
        }
        return Msg.success("取消收藏成功");
    }

}
