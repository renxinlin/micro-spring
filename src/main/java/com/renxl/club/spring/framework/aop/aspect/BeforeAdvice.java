package com.renxl.club.spring.framework.aop.aspect;

import com.renxl.club.spring.framework.aop.interceptor.MethodInterceptor;
import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;

/**
 * @Author renxl
 * @Date 2020-04-23 19:16
 * @Version 1.0.0
 */
public class BeforeAdvice extends AbstartAdvice implements Advice, MethodInterceptor {

    @Override
    public Object execute(MethodInvocation invocation) throws Throwable {
        return null;
    }
}