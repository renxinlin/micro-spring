package com.renxl.club.spring.framework.aop.aoproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author renxl
 * @Date 2020-04-22 19:45
 * @Version 1.0.0
 */
public class CglibAopProxy implements InvocationHandler,AopProxy {
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return null;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        return null;
    }
}
