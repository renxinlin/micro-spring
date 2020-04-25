package com.renxl.club.spring.framework.aop.aspect;

import com.renxl.club.spring.framework.aop.interceptor.MethodInterceptor;
import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;

import java.lang.reflect.Method;

/**
 * @Author renxl
 * @Date 2020-04-23 19:16
 * @Version 1.0.0
 */
public class AroundAdvice extends AbstartAdvice implements Advice, MethodInterceptor {
    public AroundAdvice() {
    }

    public AroundAdvice(Method aspectMethod, Object aspectTarget) {
        super(aspectMethod, aspectTarget);
    }

    /**
     * spring 的 ProceedingJoinPoint
     * 控制方法调用
     * 修改返回对象
     * 获取方法参数
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object execute(MethodInvocation invocation) throws Throwable {
        // 这里不需要调用 invocation。proceed
        // invocation相当于 joinpoint，传递给业务方，业务自己调用

        // 环绕不同于其他的业务 执行的时候把MethodInvocation交给了业务方法
        // 同时业务方法在执行mi的时候会继续返回到拦截器链的调用
        Object result = super.invokeAdviceMethod(invocation, null, null);
        return result;
    }
}
