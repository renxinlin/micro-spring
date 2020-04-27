package com.renxl.club.spring.framework.aop.aoproxy;

import java.util.stream.Collectors;

import com.renxl.club.spring.framework.aop.aspect.Advice;
import com.renxl.club.spring.framework.aop.aspect.AfterAdvice;
import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;
import com.renxl.club.spring.framework.aop.support.AdvisedSupport;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * @Author renxl
 * @Date 2020-04-22 19:45
 * @Version 1.0.0
 */
public class CglibAopProxy implements MethodInterceptor, AopProxy {
    private AdvisedSupport advisedSupport;

    public CglibAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    @Override
    public Object getProxy() {
        return getProxy(advisedSupport.getClass().getClassLoader());
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
     * Object object = methodProxy.invokeSuper(o, args);
     *
     * @param o           代理类
     * @param method      原生方法
     * @param args        方法参数
     * @param methodProxy 目标方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        List<Advice> interceptors = this.advisedSupport.getAllAdvices(method, this.advisedSupport.getTargetClass());

        List<Advice> notAfter = interceptors.stream().filter(interceptor -> !(interceptor instanceof AfterAdvice)).collect(Collectors.toList());
        List<Advice> after = interceptors.stream().filter(interceptor -> interceptor instanceof AfterAdvice).collect(Collectors.toList());
        MethodInvocation methodInvocation = new MethodInvocation(method, advisedSupport.getTarget(), args, notAfter, after, advisedSupport.getTargetClass(), new HashMap());
        Object proceed = methodInvocation.proceed();

        for (Advice advise : after) {

            if (advise instanceof com.renxl.club.spring.framework.aop.interceptor.MethodInterceptor) {
                com.renxl.club.spring.framework.aop.interceptor.MethodInterceptor methodInterceptor = (com.renxl.club.spring.framework.aop.interceptor.MethodInterceptor) advise;
                methodInterceptor.execute(methodInvocation);
            }
        }
        return proceed;
    }


    /**
     * 从代理获取原始
     *
     * @param proxy
     * @return
     * @throws Exception
     */
    public static Object getTarget(Object proxy) {
        try {
            Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            Object cglibAopProxy = h.get(proxy);
            Field advisedSupport = cglibAopProxy.getClass().getDeclaredField("advisedSupport");
            advisedSupport.setAccessible(true);
            AdvisedSupport advisedSupportObject = (AdvisedSupport) advisedSupport.get(cglibAopProxy);
            return advisedSupportObject.getTarget();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("");
        }

    }


}
