package com.hth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hth.entity.Post;
import com.hth.entity.PostDetail;
import io.swagger.models.auth.In;

import java.util.List;

public interface PostService {
    int addPost(Post post);


    PageInfo findAll(Integer page, Integer size);

    PostDetail findPostById(Integer id);

    PageInfo findTypeById(Integer id, Integer page, Integer size);

    void update( Post post);

    List<PostDetail> findPostByUserId(Integer id);

    boolean delete(Integer id);
    void adminDelete(Integer id);

    PostDetail findPostByIds(Integer id);

    List<PostDetail> findFollowingPost(List<Integer> uidList);

    List<PostDetail> serachPost(String content);

    int rePost(Integer id);

    Boolean changeStatus(Integer id);
}