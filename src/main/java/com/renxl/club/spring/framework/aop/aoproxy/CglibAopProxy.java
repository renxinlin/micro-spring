package com.renxl.club.spring.framework.aop.aoproxy;
import	java.util.HashMap;

import com.renxl.club.spring.framework.aop.aspect.Advice;
import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;
import com.renxl.club.spring.framework.aop.support.AdvisedSupport;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author renxl
 * @Date 2020-04-22 19:45
 * @Version 1.0.0
 */
public class CglibAopProxy implements MethodInterceptor,AopProxy {
    private AdvisedSupport advisedSupport;

    public CglibAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return null;
    }

    @Override
    public Object getProxy(ClassLoader classLoader) {
//        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/");
        //创建Enhancer对象，类似于JDK动态代理的Proxy类，下一步就是设置几个参数
        Enhancer enhancer = new Enhancer();
        //设置目标类的字节码文件
        enhancer.setSuperclass(advisedSupport.getTargetClass());
        //设置回调函数
        enhancer.setCallback(this);
        return enhancer.create();

    }

    /**
     *        Object object = methodProxy.invokeSuper(o, args);
     * @param o  代理类
     * @param method 原生方法
     * @param args 方法参数
     * @param methodProxy 目标方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        List<Advice> interceptors = this.advisedSupport.getAllAdvices(method,this.advisedSupport.getTargetClass());
        MethodInvocation methodInvocation = new MethodInvocation(method, advisedSupport.getTarget(),args,interceptors,advisedSupport.getTargetClass(),new HashMap());
        return methodInvocation.proceed();
    }
}
