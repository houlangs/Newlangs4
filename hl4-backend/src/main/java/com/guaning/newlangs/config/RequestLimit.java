package com.guaning.newlangs.config;

import java.lang.annotation.*;

@Documented
@Inherited
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestLimit {
	// 在 second 秒内，最大只能请求 maxCount 次
    long second() default 1;
    int maxCount() default 1;
}
