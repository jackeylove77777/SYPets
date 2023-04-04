package com.hth.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hth.entity.Post;
import com.hth.entity.PostDetail;
import com.hth.mapper.PostMapper;
import com.hth.service.PostService;
import com.hth.util.BeanCopyUtils;
import com.hth.util.JWTUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    @Autowired
    PostMapper postMapper;
    @Autowired
    RedisTemplate redisTemplate;
    /**
     * 发布文章
     * @param post
     * @return
     */
    @Override
    public int addPost(Post post) {
        Integer userId = JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        post.setUid(userId);
        post.setCreateTime(new Date());
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    public PageInfo findAll(Integer current, Integer size) {
        PageHelper.startPage(current,size);
        List<PostDetail> postDetais=postMapper.findAll();
        for(PostDetail p:postDetais){
            p=getDetail(p);
        }
        PageInfo pageInfo=new PageInfo(postDetais);
        return pageInfo;
    }

    /**
     * 通过文章id找出文章
     * @param id
     * @return
     */
    @Override
    public PostDetail findPostById(Integer id) {
        PostDetail p=postMapper.findPostById(id);
        if(p==null)
            return null;
        p=getDetail(p);
        return p;
    }

    /**
     * 找出某一类型的文章
     *
     * @param id
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo findTypeById(Integer id, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        List<PostDetail> postDetais=postMapper.findTypeById(id);

        for(PostDetail p:postDetais){
            p=getDetail(p);
        }
        PageInfo pageInfo=new PageInfo(postDetais);
        return pageInfo;
    }

    @Override
    public void update(Post post) {
        postMapper.updateById(post);
    }

    /**
     * 找出某用户的发布的所有文章
     * @param id
     * @return
     */
    @Override
    public List<PostDetail> findPostByUserId(Integer id) {
        List<PostDetail> postDetails=postMapper.findPostByUserId(id);
        if(postDetails.size()==0||postDetails==null)
            return null;
        for(PostDetail p:postDetails){
            p=getDetail(p);
        }
        return postDetails;
    }

    @Override
    public boolean delete(Integer id) {
        //只有作者本人才能删除或者Admin
        Post p = findPostById(id);
        if(p.getUid().equals(JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal()))){
            postMapper.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void adminDelete(Integer id) {
        postMapper.deleteById(id);
    }

    @Override
    public PostDetail findPostByIds(Integer id) {
        return findPostById(id);
    }

    /**
     * 找出关注的人发布的文章
     * @param uidList 关注的用户id集合
     * @return
     */
    @Override
    public List<PostDetail> findFollowingPost(List<Integer> uidList) {
        List<PostDetail> followingPost = postMapper.findFollowingPost(uidList);
        for(PostDetail p:followingPost){
            getDetail(p);
        }
        return followingPost;
    }


    @Override
    public List<PostDetail> serachPost(String content) {
        List<PostDetail> posts = postMapper.serachPost("%"+content+"%");
        for(PostDetail p:posts){
            getDetail(p);
        }
        return posts;
    }

    @Override
    public int rePost(Integer id) {
        return 0;
    }

    @Override
    public Boolean changeStatus(Integer id) {
        Post post = postMapper.selectById(id);
        if(post==null)
            return false;
        if(post.getStatus()==1)
            post.setStatus(0);
        else
            post.setStatus(1);
        postMapper.updateById(post);
        return true;
    }


    /**
     * 获取文章的详细信息 收藏、点赞这些
     * @param p
     * @return
     */
    public PostDetail getDetail(PostDetail p){
        int userId= JWTUtil.getUserId((String) SecurityUtils.getSubject().getPrincipal());
        p.setStarts(redisTemplate.opsForSet().size("post:" + p.getId() + ":start").intValue());
        p.setStart(redisTemplate.opsForSet().isMember("post:" + p.getId() + ":start",userId).booleanValue());
        p.setCollects(redisTemplate.opsForSet().size("post:" + p.getId() + ":collect").intValue());
        p.setCollect(redisTemplate.opsForSet().isMember("post:" + p.getId() + ":collect",userId).booleanValue());
        return p;
    }
}