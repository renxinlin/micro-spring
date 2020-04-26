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
    private boolean isproxy  ;
    private int proxyType  ;

    public BeanWrapper(Object wrappedInstance,boolean isproxy,int proxyType){
        this.wrappedInstance = wrappedInstance;
        this.wrappedClass = wrappedInstance.getClass();
        this.isproxy = isproxy;
        this.proxyType = proxyType;
    }

    public Object getWrappedInstance(){
        return this.wrappedInstance;
    }

    public boolean isIsproxy() {
        return isproxy;
    }

    public boolean isJdkProxy(){
        return this.proxyType == 1;

    }
    public boolean isCglibProxy(){
        return this.proxyType == -1;
    }
}
