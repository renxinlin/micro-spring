package com.renxl.club.spring.framework.aop.interceptor;

import com.renxl.club.spring.framework.aop.aspect.JoinPoint;

import java.lang.reflect.Method;

/**
 * @Author renxl
 * @Date 2020-04-22 20:05
 * @Version 1.0.0
 *
 * 该类封装了 support的拦截器链 并完成拦截器链和连接点的正确执行
 */
public class MethodInvocation implements JoinPoint {
    @Override
    public Object getThis() {
        return null;
    }

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }

    @Override
    public Method getMethod() {
        return null;
    }

    @Override
    public void setUserAttribute(String key, Object value) {

    }

    @Override
    public Object getUserAttribute(String key) {
        return null;
    }
}
