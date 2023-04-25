package com.hth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hth.entity.Findpet;
import com.hth.entity.Foster;


/**
 * (Foster)表服务接口
 *
 * @author makejava
 * @since 2023-03-10 23:34:12
 */
public interface FosterService extends IService<Foster> {
    Foster getById(int id);
    boolean removeById(int id);
    boolean save(Foster foster);
}
