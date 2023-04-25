package com.hth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hth.entity.Findpet;


/**
 * (Findpet)表服务接口
 *
 * @author makejava
 * @since 2023-04-02 00:10:52
 */
public interface FindpetService extends IService<Findpet> {
    Findpet getById(int id);
    boolean removeById(int id);
    boolean save(Findpet findpet);
}
