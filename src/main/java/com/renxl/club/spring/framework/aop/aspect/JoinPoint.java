package com.renxl.club.spring.framework.aop.aspect;

import java.lang.reflect.Method;

/**
 *
 *
 *
 * 注意目前没有ProceedingJoinPoint
 * JoinPoint是包含了PJP的能力的
 * todo 未来会关闭该能力的暴露
 * 定义面向用户的接口
 *
 * @Author renxl
 * @Date 2020-04-22 20:06
 * @Version 1.0.0
 */
public interface JoinPoint {

    Object getThis();

    Object[] getArguments();

    Method getMethod();

    void setUserAttribute(String key, Object value);

    Object getUserAttribute(String key);
}
