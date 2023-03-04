package com.hth.service;

import com.hth.entity.Answer;
import com.hth.entity.Comment;

import java.util.List;

public interface CommentService {
    Comment comment(Integer pid,String content);

    List<Comment> findCommentByPid(Integer id);

    Answer answer(Integer commentId, String content);
}
