package com.renxl.club.spring.framework.aop.aspect;

import com.renxl.club.spring.framework.aop.interceptor.MethodInterceptor;
import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @Author renxl
 * @Date 2020-04-23 19:16
 * @Version 1.0.0
 */
public class AfterThrowingAdvice extends AbstartAdvice implements Advice, MethodInterceptor {
    public AfterThrowingAdvice() {
    }

    public AfterThrowingAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    @Override
    public Object execute(MethodInvocation invocation) throws Throwable {

        try {
            return invocation.proceed();
        } catch (Exception e) {
            e.printStackTrace();
            return invokeAdviceMethod(invocation,null,e);
        }
    }
}
