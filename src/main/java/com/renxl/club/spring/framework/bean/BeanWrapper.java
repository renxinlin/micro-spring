package com.renxl.club.spring.framework.bean;

/**
 *  wraper 代理类和原始单例的实例
 * @Author renxl
 * @Date 2020-04-16 14:31
 * @Version 1.0.0
 */
public class BeanWrapper {
    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public BeanWrapper(Object wrappedInstance){
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass = wrappedInstance.getClass();
    }

    public Object getWrappedInstance(){
        return this.wrappedInstance;
    }

    public Class<?> getWrappedClass(){
        return this.wrappedInstance.getClass();
    }
}
