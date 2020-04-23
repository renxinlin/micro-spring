package com.renxl.club.spring.framework.aop.aoproxy;

import com.renxl.club.spring.framework.aop.support.AdvisedSupport;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author renxl
 * @Date 2020-04-22 19:45
 * @Version 1.0.0
 */
public class CglibAopProxy implements MethodInterceptor,AopProxy {
    private AdvisedSupport advisedSupport;

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

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        //注意这里的方法调用
        Object object = methodProxy.invokeSuper(o, args);
        return object;
    }
}
