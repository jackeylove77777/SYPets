package com.hth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hth.entity.Type;
import com.hth.mapper.TypeMapper;
import com.hth.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TypeServiceImpl implements TypeService {
    @Autowired
    TypeMapper typeMapper;
    @Override
    public List<Type> findTypeList() {
        return typeMapper.selectList(new LambdaQueryWrapper<Type>());
    }

    /**
     * 找出文章类型的数量
     * @return
     */
    @Override
    public List<Type> findTypeListNums() {
        return typeMapper.findTypeListNums();
    }

    @Override
    public Integer findTypeIdByName(String name) {
        LambdaQueryWrapper<Type> typeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        typeLambdaQueryWrapper.eq(Type::getName,name);
        Type type = typeMapper.selectOne(typeLambdaQueryWrapper);
        return type.getId();
    }
}
