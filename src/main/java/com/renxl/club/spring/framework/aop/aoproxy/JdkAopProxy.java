package com.renxl.club.spring.framework.aop.aoproxy;

import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;
import com.renxl.club.spring.framework.aop.support.AdvisedSupport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author renxl
 * @Date 2020-04-22 19:45
 * @Version 1.0.0
 */
public class JdkAopProxy  implements InvocationHandler,AopProxy {
    private AdvisedSupport advisedSupport;
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MethodInvocation methodInvocation = new MethodInvocation();
        return null;
    }

    @Override
    public Object getProxy() {
        return getProxy(advisedSupport.getClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        Object instance = Proxy.newProxyInstance(classLoader,advisedSupport.getTarget().getClass().getInterfaces(),this);
        return instance;
    }
}
