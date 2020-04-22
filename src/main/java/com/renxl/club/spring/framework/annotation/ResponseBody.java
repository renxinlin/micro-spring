package com.renxl.club.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Author renxl
 * @Date 2020-04-20 19:52
 * @Version 1.0.0
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {
    String value() default "";
}