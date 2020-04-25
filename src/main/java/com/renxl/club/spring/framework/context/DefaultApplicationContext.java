package com.renxl.club.spring.framework.context;

import java.security.KeyStore.Builder;

import com.renxl.club.spring.framework.annotation.Autowired;
import com.renxl.club.spring.framework.annotation.Controller;
import com.renxl.club.spring.framework.annotation.Service;
import com.renxl.club.spring.framework.aop.aoproxy.AopProxy;
import com.renxl.club.spring.framework.aop.aoproxy.CglibAopProxy;
import com.renxl.club.spring.framework.aop.aoproxy.JdkAopProxy;
import com.renxl.club.spring.framework.aop.config.AopConfigHolder;
import com.renxl.club.spring.framework.aop.support.AdvisedSupport;
import com.renxl.club.spring.framework.bean.BeanDefinition;
import com.renxl.club.spring.framework.bean.BeanWrapper;
import com.renxl.club.spring.framework.bean.support.DefaultBeanDefinitionReader;
import com.renxl.club.spring.framework.context.support.AbstractApplicationContext;
import com.renxl.club.spring.framework.util.StringManager;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @Author renxl
 * @Date 2020-04-16 14:45
 * @Version 1.0.0
 */
public class DefaultApplicationContext extends AbstractApplicationContext {

    /**
     * 用prop替代xml方便解析 既不是 xml版本也不是注解版，default采用的是properties版本
     * application.p
     */
    private String propertiesFile; //  applitaion-a.p,applitaion-a.p,


    private AopConfigHolder aopConfigHolder = new AopConfigHolder();

    public DefaultApplicationContext(String propertiesFile) {
        this.propertiesFile = propertiesFile;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void refresh() {
        reader = new DefaultBeanDefinitionReader(propertiesFile, this);
        /**
         * spring本身三步走
         * 定位 xml->resource
         * 加载 resouce->bd
         * 注册 bd注册到容器
         */
        List<BeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        // todo bdrpp bfpp
        doRegisterBeanDefinition(beanDefinitions);

        // TODO 广播器事件

        // 实例化
        finishBeanFactoryInitialization();
    }

    private void finishBeanFactoryInitialization() {
        for (Map.Entry<String, BeanDefinition> beanDefinitionEntry : beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                getBean(beanName);
            }
        }
    }

    /**
     * es 统计 count 分页 | 性能
     *
     * @param beanDefinitions
     */

    private void doRegisterBeanDefinition(List<BeanDefinition> beanDefinitions) {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            // 采用全限定名的原因 ，防止不同包下的类名相同发生冲突 第一版容器beanname容器而不是beanType容器
            BeanDefinition existed = beanDefinitionMap.get(beanDefinition.getReferenceBeanName());
            if (existed != null) {
                throw new RuntimeException(StringManager.getString(""));
            }
            beanDefinitionMap.put(beanDefinition.getBeanName(), beanDefinition);
        }
    }


    @Override
    public Object getBean(String beanName) {
        if (singletonCache.containsKey(beanName)) {
            return singletonCache.get(beanName);
        }
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        Object instance = null;
        // todo 可以提供spring.factory这样的功能
//        BeanPostProcessor postProcessor =
//        postProcessor.postProcessBeforeInitialization(instance,beanName);

        instance = instantiateBean(beanName, beanDefinition);

        BeanWrapper beanWrapper = new BeanWrapper(instance);

        singletonWrapperCache.put(beanName, beanWrapper);

//        postProcessor.postProcessAfterInitialization(instance,beanName);
        populateBean(beanName, new BeanDefinition(), beanWrapper);
        return this.singletonWrapperCache.get(beanName).getWrappedInstance();
    }

    private void populateBean(String beanName, BeanDefinition beanDefinition, BeanWrapper beanWrapper) {


        Object instance = beanWrapper.getWrappedInstance();


        Class<?> clazz = beanWrapper.getWrappedClass();
        //判断只有加了注解的类，才执行依赖注入
        if (!(clazz.isAnnotationPresent(Controller.class) || clazz.isAnnotationPresent(Service.class))) {
            return;
        }

        //获得所有的fields
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Autowired.class)) {
                continue;
            }

            Autowired autowired = field.getAnnotation(Autowired.class);

            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getSimpleName();
            }
            //强制访问
            field.setAccessible(true);
            try {
                //为什么会为NULL，先留个坑
                if (this.singletonCache.get(autowiredBeanName) == null) {
                    continue;
                }
                field.set(instance, this.singletonCache.get(autowiredBeanName));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }

    }

    private Object instantiateBean(String beanName, BeanDefinition beanDefinition) {
        String className = beanDefinition.getBeanClass().getName();
        Object instance = null;
        try {
            Class<?> clazz = Class.forName(className);
            instance = clazz.newInstance();
            // todo aop 和factorybean  都不可以少 factorybean+bdpp 是个非常重要的扩展点

            AdvisedSupport advisedSupport = new AdvisedSupport();
            advisedSupport.setConfigHolder(aopConfigHolder);
            advisedSupport.setTargetClass(clazz); // 低层解析拦截器链 切人点
            advisedSupport.setTarget(instance);
            boolean isAop = advisedSupport.pointCutMatch(); // 是否匹配切入点表达式
            if (isAop) {
                // 先加入aop
                Class<?>[] interfaces = clazz.getInterfaces();
                AopProxy proxy = null;
                if (interfaces == null || interfaces.length == 0) {
                    proxy = new CglibAopProxy(advisedSupport);
                } else {
                    proxy = new JdkAopProxy(advisedSupport);
                }
                instance = proxy.getProxy();
            }

            this.singletonCache.put(beanName, instance);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 实现aware
        if (instance instanceof ApplicationContextAware) {
            ((ApplicationContextAware) instance).setApplicationContext(this);
        }
        return instance;

    }


    // todo 暂时不支持

    /**
     * 目前设计的方向是构建一个二级索引结构
     * 所有的索引都指向singleton缓冲池
     *
     * @param beanClass
     * @return
     */
    @Deprecated
    @Override
    public Object getBean(Class<?> beanClass) {
        return null;
    }


    public AopConfigHolder getAopConfigHolder() {
        return aopConfigHolder;
    }
}
