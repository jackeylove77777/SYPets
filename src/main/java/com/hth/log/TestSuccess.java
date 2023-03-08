package com.hth.log;

import java.lang.annotation.*;

//方便调试过程中，标注已经成功调试的方法
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TestSuccess {
}
