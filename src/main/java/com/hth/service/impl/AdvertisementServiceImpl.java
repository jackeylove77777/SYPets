package com.hth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.entity.Advertisement;
import com.hth.mapper.AdvertisementMapper;
import com.hth.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 友链(Advertisement)表服务实现类
 *
 * @author makejava
 * @since 2023-03-08 11:07:19
 */
@Service("advertisementService")
public class AdvertisementServiceImpl extends ServiceImpl<AdvertisementMapper, Advertisement> implements AdvertisementService {
    @Autowired
    AdvertisementMapper advertisementMapper;
    @Override
    public void add(Advertisement ad) {
        ad.setCreateTime(new Date());
        advertisementMapper.insert(ad);
    }

    @Override
    public List<Advertisement> getAll() {
        return advertisementMapper.selectList(new LambdaQueryWrapper<>());
    }
}
