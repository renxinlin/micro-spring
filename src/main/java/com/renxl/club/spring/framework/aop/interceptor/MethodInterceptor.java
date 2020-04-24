package com.renxl.club.spring.framework.aop.interceptor;

/**
 * @Author renxl
 * @Date 2020-04-22 20:05
 * @Version 1.0.0
 */
public interface MethodInterceptor {

    /**
     * 负责执行通知 链路和方法
     * 因为是链路[可能存在多个切入] 所以引入MethodInvocation ，他是专门解决aop正确执行的问题
     * @param invocation
     * @return
     * @throws Throwable
     */
    Object execute(MethodInvocation invocation) throws Throwable;

}
