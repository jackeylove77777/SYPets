package com.hth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hth.entity.Post;
import com.hth.entity.PostDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 文章帖子表(Post)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-20 23:59:25
 */
public interface PostMapper extends BaseMapper<Post> {
    List<PostDetail> findAll();
    PostDetail findPostById(Integer id);

    List<PostDetail> findTypeById(@Param("id") Integer id);



    List<PostDetail> findPostByUserId(Integer id);

    List<PostDetail> findFollowingPost(List<Integer> list);

    List<PostDetail> serachPost(String content);
}
