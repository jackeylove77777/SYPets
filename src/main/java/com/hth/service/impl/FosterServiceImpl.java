package com.hth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hth.entity.Foster;
import com.hth.mapper.FosterMapper;
import com.hth.service.FosterService;
import org.springframework.stereotype.Service;

/**
 * (Foster)表服务实现类
 *
 * @author makejava
 * @since 2023-03-10 23:34:12
 */
@Service("fosterService")
public class FosterServiceImpl extends ServiceImpl<FosterMapper, Foster> implements FosterService {

}
