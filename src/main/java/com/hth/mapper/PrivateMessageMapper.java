package com.hth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hth.chat.entity.PrivateMessage;
import org.apache.ibatis.annotations.Mapper;


/**
 * (PrivateMessage)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-11 20:23:41
 */
@Mapper
public interface PrivateMessageMapper extends BaseMapper<PrivateMessage> {

}
