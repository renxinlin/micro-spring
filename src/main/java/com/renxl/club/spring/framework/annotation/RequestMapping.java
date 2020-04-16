package com.renxl.club.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Author renxl
 * @Date 2020-04-16 15:28
 * @Version 1.0.0
 */

@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
    boolean required() default true;

}
