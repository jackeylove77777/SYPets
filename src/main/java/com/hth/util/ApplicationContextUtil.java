package com.hth.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 全局工具类，通过bean的名字获取bean
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {
    private  static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context=applicationContext;
    }
    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

}
