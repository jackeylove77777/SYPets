package com.hth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.entity.Findpet;
import com.hth.entity.Foster;
import com.hth.mapper.FosterMapper;
import com.hth.service.FosterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (Foster)表服务实现类
 *
 * @author makejava
 * @since 2023-03-10 23:34:12
 */
@Service("fosterService")
public class FosterServiceImpl extends ServiceImpl<FosterMapper, Foster> implements FosterService {
    @Autowired
    FosterMapper fosterMapper;
    @Override
    public Foster getById(int id) {
        return fosterMapper.selectById(id);
    }

    @Override
    public boolean removeById(int id) {
        int rows = fosterMapper.deleteById(id);
        return rows>0;
    }

    @Override
    public boolean save(Foster foster) {
        int rows = fosterMapper.insert(foster);
        return rows>0;
    }
}
