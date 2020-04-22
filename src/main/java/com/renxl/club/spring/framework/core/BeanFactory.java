package com.renxl.club.spring.framework.core;

/**
 * @Author renxl
 * @Date 2020-04-16 14:57
 * @Version 1.0.0
 */
public interface BeanFactory {
    Object getBean(String beanName);

    Object getBean(Class<?> beanClass);
}
