package com.hth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hth.entity.Advertisement;

import java.util.List;


/**
 * 友链(Advertisement)表服务接口
 *
 * @author makejava
 * @since 2023-03-08 11:07:19
 */
public interface AdvertisementService extends IService<Advertisement> {
    void add(Advertisement ad);
    List<Advertisement> getAll();
}
