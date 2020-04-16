package com.renxl.club.spring.framework.context
        ;

import com.renxl.club.spring.framework.context.support.AbstractApplicationContext;
import com.renxl.club.spring.framework.core.BeanFactory;

/**
 * @Author renxl
 * @Date 2020-04-16 14:45
 * @Version 1.0.0
 */
public class DefaultApplicationContext extends AbstractApplicationContext   {

    /**
     * 用prop替代xml方便解析 既不是 xml版本也不是注解版，default采用的是properties版本
     */
    private String [] propertiesFile;

    @Override
    public void refresh() {

    }



    @Override
    public Object getBean(String beanName) throws Exception {
        return null;
    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return null;
    }
}
