package com.renxl.club.spring.framework.aop.support;

import com.renxl.club.spring.framework.aop.config.AopConfig;
import com.renxl.club.spring.framework.aop.config.AopConfigHolder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 *
 *
 * 封装了拦截器链   单例对象信息
 * 相关aop切面信息
 * 为aopproxy提供封装对象，支持代理类创建
 * @Author renxl
 * @Date 2020-04-22 20:01
 * @Version 1.0.0
 */
public class AdvisedSupport {

    /**
     * 连接点所属对象的目标类
     */
    private Class targetClass;



    private Map<String, Object> pointCutClassPattern;


    /**
     * 连接点所属对象
     */
    private Object target;
    /**
     * 封装切面信息
     */
    private AopConfigHolder configHolder;

    /**
     * method和method对应的拦截器链路
     */
    private transient Map<Method, List<Object>> methodAndMethodInterceptors;

    /**
     * todo  重新处理
     * @param method
     * @param targetClass
     * @return
     * @throws NoSuchMethodException
     */
    public List<Object> getAllAdvices(Method method, Class<?> targetClass) throws NoSuchMethodException {
        List<Object> interceptors = methodAndMethodInterceptors.get(method);
        if(interceptors == null){
            Method m = targetClass.getMethod(method.getName(),method.getParameterTypes());
            interceptors = methodAndMethodInterceptors.get(m);
            this.methodAndMethodInterceptors.put(m,interceptors);
        }
        return interceptors;
    }



    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }

    public Map<String, Object> getPointCutClassPattern() {
        return pointCutClassPattern;
    }

    public void setPointCutClassPattern(Map<String, Object> pointCutClassPattern) {
        this.pointCutClassPattern = pointCutClassPattern;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public AopConfigHolder getConfigHolder() {
        return configHolder;
    }

    public void setConfigHolder(AopConfigHolder configHolder) {
        this.configHolder = configHolder;
    }

    public Map<Method, List<Object>> getMethodAndMethodInterceptors() {
        return methodAndMethodInterceptors;
    }

    public void setMethodAndMethodInterceptors(Map<Method, List<Object>> methodAndMethodInterceptors) {
        this.methodAndMethodInterceptors = methodAndMethodInterceptors;
    }
}
