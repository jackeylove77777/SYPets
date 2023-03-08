package com.hth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hth.entity.Advertisement;
import org.apache.ibatis.annotations.Mapper;


/**
 * 友链(Advertisement)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-08 11:07:19
 */
@Mapper
public interface AdvertisementMapper extends BaseMapper<Advertisement> {

}
