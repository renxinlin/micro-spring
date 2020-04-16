package com.renxl.club.spring.framework.annotation;

import java.lang.annotation.*;

/**
 * @Author renxl
 * @Date 2020-04-16 15:26
 * @Version 1.0.0
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    String value() default "";

}
