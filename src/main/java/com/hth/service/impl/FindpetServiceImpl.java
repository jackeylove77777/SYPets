package com.hth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.entity.Findpet;
import com.hth.mapper.FindpetMapper;
import com.hth.service.FindpetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (Findpet)表服务实现类
 *
 * @author makejava
 * @since 2023-04-02 00:10:52
 */
@Service("findpetService")
public class FindpetServiceImpl extends ServiceImpl<FindpetMapper, Findpet> implements FindpetService {
    @Autowired
    FindpetMapper findpetMapper;

    @Override
    public Findpet getById(int id) {
        return findpetMapper.selectById(id);
    }

    @Override
    public boolean removeById(int id) {
        int rows = findpetMapper.deleteById(id);
        return rows>0;
    }

    @Override
    public boolean save(Findpet findpet) {
        int rows = findpetMapper.insert(findpet);
        return rows>0;
    }
}
