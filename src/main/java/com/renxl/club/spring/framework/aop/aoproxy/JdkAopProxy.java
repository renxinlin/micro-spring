package com.renxl.club.spring.framework.aop.aoproxy;

import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;
import com.renxl.club.spring.framework.aop.support.AdvisedSupport;

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
public class JdkAopProxy  implements InvocationHandler,AopProxy {
    private AdvisedSupport advisedSupport;



    /**
     * 该方法负责集中处理动态代理类上的所有方法调用。
     * 调用处理器根据这三个参数进行预处理或分派到委托类实例上反射执行
     *
     * @param proxy  代理类实例
     * @param method 被调用的方法对象
     * @param args   调用参数
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        List<Object> interceptors = this.advisedSupport.getAllAdvices(method,this.advisedSupport.getTargetClass());
        MethodInvocation methodInvocation = new MethodInvocation(method, advisedSupport.getTarget(),args,interceptors,advisedSupport.getTargetClass(),new HashMap());
        return methodInvocation.proceed();
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
