package com.hth.config;


import com.hth.filter.ExceptionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 捕获Filter内部产生的异常，因为springboot全局捕获异常捕获
 * 不到Filter里面产生的异常
 */
@Component
public class FilterExceConfig {
    @Autowired
    ExceptionFilter exceptionFilter;
    @Bean
    public FilterRegistrationBean exceptionFilterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.addUrlPatterns("/*");
        registration.setFilter(exceptionFilter);
        registration.setName("exceptionFilter");
        //此处尽量小，要比其他Filter靠前
        registration.setOrder(-1);
        return registration;
    }
}
