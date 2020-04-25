package com.renxl.club.spring.framework.aop.aspect;

import com.renxl.club.spring.framework.aop.interceptor.MethodInterceptor;
import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @Author renxl
 * @Date 2020-04-23 19:16
 * @Version 1.0.0
 */
public class AfterReturnAdvice extends AbstartAdvice implements Advice, MethodInterceptor {
    public AfterReturnAdvice() {
    }

    public AfterReturnAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    /**
     * 可以修改返回值
     * 顺序早于after
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object execute(MethodInvocation invocation) throws Throwable {
        Object proceed = invocation.proceed();
        proceed = invokeAdviceMethod(invocation, proceed, null);
        return proceed;
    }
}
