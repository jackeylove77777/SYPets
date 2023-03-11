package com.hth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.entity.Stray;
import com.hth.mapper.StrayMapper;
import com.hth.service.StrayService;
import org.springframework.stereotype.Service;

/**
 * (Stray)表服务实现类
 *
 * @author makejava
 * @since 2023-03-10 23:35:54
 */
@Service("strayService")
public class StrayServiceImpl extends ServiceImpl<StrayMapper, Stray> implements StrayService {

}
