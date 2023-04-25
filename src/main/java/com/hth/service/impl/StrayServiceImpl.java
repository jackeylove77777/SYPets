package com.hth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.entity.Foster;
import com.hth.entity.Stray;
import com.hth.mapper.StrayMapper;
import com.hth.service.StrayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (Stray)表服务实现类
 *
 * @author makejava
 * @since 2023-03-10 23:35:54
 */
@Service("strayService")
public class StrayServiceImpl extends ServiceImpl<StrayMapper, Stray> implements StrayService {

    @Autowired
    StrayMapper strayMapper;
    @Override
    public Stray getById(int id) {
        return strayMapper.selectById(id);
    }

    @Override
    public boolean removeById(int id) {
        int rows = strayMapper.deleteById(id);
        return rows>0;
    }

    @Override
    public boolean save(Stray stray) {
        int rows = strayMapper.insert(stray);
        return rows>0;
    }
}
