//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.renxl.club.spring.framework.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Pointcut {
    // 命令如下
    // 这里我们只支持基于注解的切入点@annotation(com.renxl.Logger)
    // 以及基于指定包的切人点  "@package(com.renxl.club)"
    String value() default "";
    String name() default "";

}
