package com.hth.log;

import java.lang.annotation.*;

//写后端时以为前端要用到的方法，结果没用到
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotUse {
}
