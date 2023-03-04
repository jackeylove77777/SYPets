package com.hth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hth.entity.Answer;
import com.hth.entity.Comment;
import com.hth.entity.PostDetail;
import com.hth.entity.User;
import com.hth.mapper.AnswerMapper;
import com.hth.mapper.CommentMapper;
import com.hth.mapper.PostMapper;
import com.hth.mapper.UserMapper;
import com.hth.service.CommentService;
import com.hth.service.MessageService;
import com.hth.util.JWTUtil;
import com.hth.util.ReplaceUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

public class CommentServiceImpl implements CommentService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MessageService messageService;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    PostMapper postMapper;
    @Autowired
    AnswerMapper answerMapper;
    @Override
    public Comment comment(Integer pid, String content) {
        Comment comment = new Comment();
        comment.setPid(pid);
        comment.setContent(content);
        //找出评论的用户信息
        int uid = JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        comment.setUid(uid);
        User user = userMapper.selectById(uid);
        comment.setUname(user.getUsername());
        comment.setAvatar(user.getAvatar());
        //添加评论
        commentMapper.insert(comment);
        PostDetail postDetail = postMapper.findPostById(pid);
        //替换成超链接
        String message= ReplaceUtil.userReplace(user.getUsername())+"在"+ReplaceUtil.PostReplace(pid,postDetail.getTitle())
                +"留下了评论";
        messageService.addMessage(postDetail.getUid(),message,1);
        return comment;
    }

    /**
     * 找出这篇文章所有的评论
     * @param id
     * @return
     */
    @Override
    public List<Comment> findCommentByPid(Integer id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getPid,id);
        List<Comment> commentList=commentMapper.selectList(queryWrapper);
        for(Comment c:commentList){
            //找出这条评论的回复内容
            LambdaQueryWrapper<Answer> answerWrapper = new LambdaQueryWrapper<>();
            answerWrapper.eq(Answer::getCommentId,c.getUid());
            c.setAnswerList(answerMapper.selectList(answerWrapper));
        }
        return commentList;
    }

    /**
     * 回复一条评论
     * @param commentId 评论id
     * @param content   内容
     * @return
     */
    @Override
    public Answer answer(Integer commentId, String content) {
        Answer answer=new Answer();
        //获取回复这个用户的信息
        User user=userMapper.selectById(JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()));
        answer.setUid(user.getId());
        answer.setUname(user.getUsername());
        answer.setAvatar(user.getAvatar());
        answer.setContent(content);
        answer.setCommentId(commentId);
        //添加回复
        answerMapper.insert(answer);
        //回复消息
        messageService.addMessage(2,commentId,user.getUsername());
        return answer;
    }
}
