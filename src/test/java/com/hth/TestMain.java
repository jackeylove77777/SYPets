package com.hth;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hth.entity.Type;
import com.hth.mapper.TypeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class TestMain {
    @Autowired
    TypeMapper typeMapper;

    @Test
    public void testTypeMapper(){
        LambdaQueryWrapper<Type> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Type::getId);
        List<Type> types = typeMapper.selectList(wrapper);
        types.forEach(System.out::println);
    }
}
