package com.hth.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;

public class RedisCacheManager implements CacheManager {
    //参数1：认证或授权存储的统一名称
    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        //System.out.println(s);
        return new RedisCache<K,V>(s);
    }
}
