package com.renxl.club.spring.framework.bean.support;

import com.renxl.club.spring.framework.bean.BeanDefinition;
import com.renxl.club.spring.framework.core.BeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author renxl
 * @Date 2020-04-16 14:41
 * @Version 1.0.0
 */
public abstract class DefaultListableBeanFactory implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();


}