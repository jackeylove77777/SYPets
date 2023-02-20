package com.hth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hth.entity.Mention;


/**
 * 消息推送:例如，点赞、关注、评论、回复评论这些功能(Mention)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-20 23:58:56
 */
public interface MentionMapper extends BaseMapper<Mention> {

}
