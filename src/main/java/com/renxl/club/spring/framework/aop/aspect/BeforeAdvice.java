package com.renxl.club.spring.framework.aop.aspect;

import com.renxl.club.spring.framework.aop.interceptor.MethodInterceptor;
import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @Author renxl
 * @Date 2020-04-23 19:16
 * @Version 1.0.0
 */
public class BeforeAdvice extends AbstartAdvice implements Advice, MethodInterceptor {
    public BeforeAdvice() {
    }

    public BeforeAdvice(Method aspectMethod, Object aspectTarget,int order) {
        super(aspectMethod, aspectTarget,order);
    }

    /**
     * 不可以修改返回值
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object execute(MethodInvocation invocation) throws Throwable {
        super.invokeAdviceMethod(invocation,null,null);
        Object proceed = invocation.proceed();
        return proceed;
    }

}
