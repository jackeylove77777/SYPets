package com.hth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hth.entity.Type;
import org.apache.ibatis.annotations.*;

import java.util.List;


/**
 * 文章的类型(Type)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-20 23:59:35
 */
@Mapper
public interface TypeMapper extends BaseMapper<Type> {
    @Select("select * from type")
    @Results(value = {
            @Result(id = true,column = "id",property = "id"),
            @Result(property = "name",column = "name"),
            @Result(property = "type_num",column = "id",one = @One(select = "com.hth.mapper.TypeMapper.findOneTypeNums"))
    })
    List<Type> findTypeListNums();

    @Select("select count(*) as type_num from post where type_id=#{id} and post.del_flag!=1")
    Integer findOneTypeNums(int id);
}
