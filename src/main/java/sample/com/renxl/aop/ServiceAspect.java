package sample.com.renxl.aop;

import com.renxl.club.spring.framework.aop.annotation.Before;
import com.renxl.club.spring.framework.aop.annotation.Pointcut;
import com.renxl.club.spring.framework.aop.aspect.JoinPoint;
import com.renxl.club.spring.framework.aop.interceptor.MethodInvocation;

/**
 * @Author renxl
 * @Date 2020-04-25 18:25
 * @Version 1.0.0
 */
public class ServiceAspect {

    @Before("pointcut")
    public void before() {
        System.out.println("=======我是before====");
    }


    @Before("pointcut")
    public void after() {
        System.out.println("=======我是After====");
    }


    @Before("pointcut")
    public Object afterReturn(Object o) {
        System.out.println("=======我是返回值===="+ o);
        return o;
    }

    @Before("pointcut")
    public void afterThrow(Throwable t) {
        System.out.println("=======我是异常====");

    }


    @Before("pointcut")
    public Object around(JoinPoint proceedJoinPoint) throws Throwable {
        System.out.println("=======我是around start====");
        MethodInvocation methodInvocation = (MethodInvocation) proceedJoinPoint;
        Object proceed = methodInvocation.proceed();
        System.out.println("=======我是around end===="+proceed);
        return proceed;
    }




    @Pointcut("@annotation(sample.com.renxl.aop.Aop)")
    public void pointcut() {
    }



}
