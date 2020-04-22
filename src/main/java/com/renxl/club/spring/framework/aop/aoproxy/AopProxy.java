package com.renxl.club.spring.framework.aop.aoproxy;

/**
 * @Author renxl
 * @Date 2020-04-22 19:44
 * @Version 1.0.0
 */
public interface AopProxy {


    Object getProxy();


    Object getProxy(ClassLoader classLoader);
}
