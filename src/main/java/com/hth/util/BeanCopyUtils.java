package com.hth.util;

import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream().map(o->BeanCopyUtils.copyBean(o,clazz)).collect(Collectors.toList());
    }
    public static <V> V copyBean(Object source,Class<V> clazz){
        V result = null;//目标对象
        try{
            result = clazz.newInstance();
            //使用Spring boot框架自带BeanUtil进行属性copy
            BeanUtils.copyProperties(source,result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
