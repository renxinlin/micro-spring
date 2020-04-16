package com.renxl.club.spring.framework.core;

/**
 * @Author renxl
 * @Date 2020-04-16 14:57
 * @Version 1.0.0
 */
public interface FactoryBean {
    /**
     * 返回生产的单例bean
     * @return
     */
    Object getObject();
}
