package com.renxl.club.spring.framework.bean;

/**
 * @Author renxl
 * @Date 2020-04-16 14:29
 * @Version 1.0.0
 */
public interface BeanPostProcessor {

     Object postProcessBeforeInitialization(Object bean, String beanName);

     Object postProcessAfterInitialization(Object bean, String beanName);
}
