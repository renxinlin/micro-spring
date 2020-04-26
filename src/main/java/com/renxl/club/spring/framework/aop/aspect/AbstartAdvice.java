package com.renxl.club.spring.framework.aop.aspect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author renxl
 * @Date 2020-04-23 18:55
 * @Version 1.0.0
 */
public class AbstartAdvice implements Advice {
    private int order = 0;

    /**
     * 通知的方法
     */
    private Method aspectMethod;


    /**
     * 通知的目标对象
     */
    private Object aspectTarget;


    public AbstartAdvice() {
    }

    public AbstartAdvice(Method aspectMethod, Object aspectTarget,int order) {
        this.aspectMethod = aspectMethod;
        this.aspectTarget = aspectTarget;
        this.order = order;
    }

    /**
     * 定义完毕通知方法的出入参
     * @return
     */
    public Object invokeAdviceMethod(JoinPoint joinPoint,Object returnObject,Throwable throwable) throws InvocationTargetException, IllegalAccessException {
        Class<?>[] parameterTypes = aspectMethod.getParameterTypes();
        Object [] args = null;
        if( parameterTypes != null ){
            args = new Object[parameterTypes.length];
            int i =0;
            for(Class parameterType : parameterTypes){

                if(parameterType == JoinPoint.class){
                    args[i] = joinPoint;
                }

                if(parameterType == Object.class){
                    args[i] = returnObject;
                }

                if(parameterType == Throwable.class){
                    args[i] = throwable;
                }
                i++;
            }
        }
        return aspectMethod.invoke(aspectTarget,args);


    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
