package com.hth.controller;

import com.hth.entity.Answer;
import com.hth.entity.Comment;
import com.hth.entity.Msg;
import com.hth.log.Logweb;
import com.hth.log.TestSuccess;
import com.hth.mapper.AnswerMapper;
import com.hth.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CommentController {
    @Autowired
    CommentService commentService;
    @PostMapping("/comment/{id}")
    @TestSuccess
    public Msg comment(@PathVariable Integer pid, @RequestBody Map<String,String> map){
        Comment item = commentService.comment(pid,map.get("content"));
        return Msg.success("评论成功").add("item",item);
    }

    /**
     *
     * @param id 查出一篇文档里的所有评论
     * @return
     */
    @GetMapping("/findCommentByPid/{id}")
    @TestSuccess
    public Msg findCommentByPid(@PathVariable Integer id){
        List<Comment> commentList = commentService.findCommentByPid(id);
        return Msg.success().add("list",commentList);
    }
    /**
     *
     * @param commentId 回复的评论ID
     * @param map 接收回复内容的
     * @return
     */
    @PostMapping("/answer/{commentId}")
    @TestSuccess
    public Msg answer(@PathVariable Integer commentId,@RequestBody Map<String,String> map){
        Answer answer = commentService.answer(commentId, map.get("content"));
        return Msg.success().add("answer",answer);
    }
}
