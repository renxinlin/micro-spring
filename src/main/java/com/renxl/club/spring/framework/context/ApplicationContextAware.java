package com.renxl.club.spring.framework.context;

import com.renxl.club.spring.framework.context.support.ApplicationContext;

/**
 *
 * aware只实现一种，也是最重要的ApplicationContextAware
 *
 * 该类作用是将当前容器中继承该接口的bean添加上当前的上下文
 * @Author renxl
 * @Date 2020-04-16 14:48
 * @Version 1.0.0
 */
public interface ApplicationContextAware  {
    void setApplicationContext(ApplicationContext applicationContext);

}
