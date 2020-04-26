package com.renxl.club.spring.framework.aop.aoproxy;

import com.renxl.club.spring.framework.aop.aspect.Advice;
import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;
import com.renxl.club.spring.framework.aop.support.AdvisedSupport;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;

/**
 * @Author renxl
 * @Date 2020-04-22 19:45
 * @Version 1.0.0
 */
public class JdkAopProxy implements InvocationHandler, AopProxy {
    private AdvisedSupport advisedSupport;

    public JdkAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    /**
     * @param proxy  代理类实例
     * @param method 被调用的方法对象
     * @param args   调用参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Advice> interceptors = this.advisedSupport.getAllAdvices(method, this.advisedSupport.getTargetClass());
        MethodInvocation methodInvocation = new MethodInvocation(method, advisedSupport.getTarget(), args, interceptors, advisedSupport.getTargetClass(), new HashMap());
        return methodInvocation.proceed();
    }

    @Override
    public Object getProxy() {
        return getProxy(advisedSupport.getClass().getClassLoader());
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
        Object instance = Proxy.newProxyInstance(classLoader, advisedSupport.getTarget().getClass().getInterfaces(), this);
        return instance;
    }

    /**
     * 从代理获取原始
     *
     * @param proxy
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy)   {
        Field field = null;
        try {
            field = proxy.getClass().getSuperclass().getDeclaredField("h");
            field.setAccessible(true);
            //获取指定对象中此字段的值
            JdkAopProxy invocationHandler = (JdkAopProxy) field.get(proxy); //获取Proxy对象中的此字段的值
            Field advisedSupport = invocationHandler.getClass().getDeclaredField("advisedSupport");

            advisedSupport.setAccessible(true);
            AdvisedSupport advisedSupportObject = (AdvisedSupport) advisedSupport.get(invocationHandler);
            return advisedSupportObject.getTarget();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }

    }
}
