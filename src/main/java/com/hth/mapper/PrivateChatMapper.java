package com.hth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hth.chat.entity.PrivateChat;
import org.apache.ibatis.annotations.Mapper;


/**
 * 私信列表的表，用于记录当前用户与谁有聊天记录(PrivateChat)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-11 19:42:07
 */
@Mapper
public interface PrivateChatMapper extends BaseMapper<PrivateChat> {

}
