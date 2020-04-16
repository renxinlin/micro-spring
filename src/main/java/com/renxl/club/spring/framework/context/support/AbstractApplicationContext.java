package com.renxl.club.spring.framework.context.support;

import com.renxl.club.spring.framework.bean.BeanWrapper;
import com.renxl.club.spring.framework.bean.support.BeanDefinitionReader;
import com.renxl.club.spring.framework.bean.support.DefaultListableBeanFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author renxl
 * @Date 2020-04-16 14:44
 * @Version 1.0.0
 */
public abstract class AbstractApplicationContext extends
        DefaultListableBeanFactory implements ApplicationContext{




    /**
     * bd 解析器
     */
    private BeanDefinitionReader reader;

    /**
     * 单例的IOC容器缓存
     */
    private Map<String,Object> singletonCache = new ConcurrentHashMap<String, Object>();
    /**
     * 封装的ioc单例缓冲
     */
    private Map<String, BeanWrapper> singletonWrapperCache = new ConcurrentHashMap<String, BeanWrapper>();



}
