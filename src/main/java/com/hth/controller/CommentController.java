package com.hth.controller;

import com.hth.entity.Comment;
import com.hth.entity.Msg;
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
    public Msg comment(@PathVariable Integer id, @RequestBody Map<String,String> map){
        Comment item = commentService.comment(id,map.get("content"));
        return Msg.success("评论成功").add("item",item);
    }

    /**
     *
     * @param id 查出一篇文档里的所有评论
     * @return
     */
    @GetMapping("/findCommentByPid/{id}")
    public Msg findCommentByPid(@PathVariable Integer id){
        List<Comment> commentList = commentService.findCommentByPid(id);
        return Msg.success().add("list",commentList);
    }
}
