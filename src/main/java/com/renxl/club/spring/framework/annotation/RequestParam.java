package com.renxl.club.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Author renxl
 * @Date 2020-04-16 15:28
 * @Version 1.0.0
 */

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface RequestParam {
    String value() default "";

}
