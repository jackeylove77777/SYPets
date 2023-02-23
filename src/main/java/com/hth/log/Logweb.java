package com.hth.log;

import java.lang.annotation.*;

/**
 * 自定义注解，当aop类发现方法上有这个注解时，将日志记录到数据库
 */
//标注在方法上的注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logweb 
{ 
    String value() default ""; 
} 