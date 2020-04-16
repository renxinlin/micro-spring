package com.renxl.club.spring.framework.bean;

import lombok.Data;

/**
 * @Author renxl
 * @Date 2020-04-16 13:47
 * @Version 1.0.0
 */
@Data
public class BeanDefinition {

    private static final String SCOPE_SINGLETON = "singleton";

    private static final String SCOPE_PROTOTYPE = "prototype";


    private volatile Object beanClass;
    /**
     * 简单类名
     */
    private String simpleBeanName;
    /**
     * 全限定类名
     */
    private String beanName;

    private String scope = SCOPE_SINGLETON;

    private boolean primary = false;

    private boolean lazyInit = false;


    /**
     * 依赖
     */
    private String[] dependsOn;

    /**
     * 初始化方法名
     */
    private String initMethodName;
    /**
     * 销毁方法名
     */
    private String destroyMethodName;
    private String description;



}
