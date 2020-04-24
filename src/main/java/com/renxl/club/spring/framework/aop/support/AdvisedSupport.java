package com.renxl.club.spring.framework.aop.support;

import com.renxl.club.spring.framework.aop.config.AopConfigHolder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

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


    /**
     * 切入点规则集合 ：目前实现包切人   注解切人
     * 不支持切人点表达式 //todo 研究下切人点的正则
     */
    private List<String> pointCut;


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

    public List<String> getPointCut() {
        return pointCut;
    }

    public void setPointCut(List<String> pointCut) {
        this.pointCut = pointCut;
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
