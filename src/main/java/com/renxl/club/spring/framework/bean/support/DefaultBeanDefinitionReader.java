package com.renxl.club.spring.framework.bean.support;


import com.renxl.club.spring.framework.annotation.Controller;
import com.renxl.club.spring.framework.annotation.Primary;
import com.renxl.club.spring.framework.annotation.Service;
import com.renxl.club.spring.framework.aop.annotation.Aspect;
import com.renxl.club.spring.framework.aop.config.AopConfig;
import com.renxl.club.spring.framework.bean.BeanDefinition;
import com.renxl.club.spring.framework.context.DefaultApplicationContext;
import com.renxl.club.spring.framework.context.support.BeanName;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * location就是需要扫描的路径
 */
public class DefaultBeanDefinitionReader implements BeanDefinitionReader {
    private List<String> allScanClasses = new ArrayList<String>();

    private Properties config = new Properties();
    private DefaultApplicationContext applicationContext;

    //固定配置文件中的key，相对于xml的规范
    private final String SCAN_PACKAGE = "scanPackages";

    public DefaultBeanDefinitionReader(String location,DefaultApplicationContext applicationContext) {
        //通过URL定位找到其所对应的文件，然后转换为文件流
        InputStream is = this.getClass().getClassLoader().getResourceAsStream(location);
        try {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        this.applicationContext = applicationContext;
        // 组件扫描
        scanner(config.getProperty(SCAN_PACKAGE));


    }

    private void scanner(String scanPackageString) {
        // path不以’/'开头时，默认是从此类所在的包下取资源；path  以’/'开头时，则是从ClassPath根下获取；

        String[] scanPackages = scanPackageString.split(",");
        for (String scanPackage : scanPackages) {
            doScanner(scanPackage);
        }
    }

    private void doScanner(String scanPackage) {
        // 当前包的资源
        URL url = DefaultBeanDefinitionReader.class.getResource("/" + scanPackage.replaceAll("\\.", "/"));
        File path = new File(url.getFile());
        File[] files = path.listFiles();
        for (File file : files) {
            // 递归
            if (file.isDirectory()) {
                doScanner(scanPackage + "." + file.getName());
            } else {
                if (file.getName().endsWith("class")) {
                    // 所有被扫描的类的全限定性类名
                    String className = (scanPackage + "." + file.getName().replace(".class", ""));
                    allScanClasses.add(className);

                    // 接下来是parse spring的常见玩法是委托 mini版直接到loadBeanDefinitions
                }

            }
        }


    }

    @Override
    public List<BeanDefinition>  loadBeanDefinitions() {
        if (allScanClasses.size() <= 0) {
            return null;
        }
        List<BeanDefinition> bds = new ArrayList<> ();

        try {
            for (String scanClass : allScanClasses) {
                Class<?> clazz = Class.forName(scanClass);
                if (!clazz.isInterface()) {
                    if(!(clazz.getDeclaredAnnotation(Service.class)!= null || clazz.getDeclaredAnnotation(Controller.class)!= null)){
                        continue;
                    }
                    BeanDefinition beanDefinition =  buildBeanDefinition(clazz);
                    bds.add(beanDefinition);

                    Primary declaredAnnotation = clazz.getDeclaredAnnotation(Primary.class);
                    boolean isprimary = declaredAnnotation!=null;

                    Class<?>[] interfaces = clazz.getInterfaces();
                    for(Class interface_ : interfaces){
                        List<BeanName> beanNames = applicationContext.getInterfacesImpl().get(interface_.getName());
                        if(beanNames == null){
                            beanNames = new ArrayList<> ();
                        }
                        // todo 处理primary注解
                        beanNames.add(new BeanName(clazz.getSimpleName(),isprimary));
                        applicationContext.getInterfacesImpl().put(interface_.getName(), beanNames);
                    }
                    // 找出所有的@aspect接口
                    if(clazz.isAnnotationPresent(Aspect.class)){
                        AopConfig aopconfig = buildAopConfig(clazz);
                        applicationContext.getAopConfigHolder().addAopConfig(aopconfig);
                    }

                }






            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return bds;
    }

    private AopConfig buildAopConfig(Class<?> clazz) {
        AopConfig aopconfig = new AopConfig();
        aopconfig.setAspectClass(clazz.getName());
        return aopconfig;
    }

    /**
     * TODO 委托给某一职责者处理
     * @param clazz
     */
    private BeanDefinition buildBeanDefinition(Class<?> clazz) {
        BeanDefinition beanDefinition = new BeanDefinition();

        beanDefinition.setBeanClass(clazz);
        // todo bean name generator
        beanDefinition.setReferenceBeanName(clazz.getName());
        // todo 定制bean 名称生成器
        beanDefinition.setBeanName(clazz.getSimpleName());
        beanDefinition.setLazyInit(false);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        // todo
        /**
         // 委托处理依赖
         beanDefinition.setDependsOn();

         // 委托处理描述
         beanDefinition.setDescription();

         beanDefinition.setInitMethodName();
         beanDefinition.setDestroyMethodName();

         // 委托处理优先注入
         beanDefinition.setPrimary();
         */
        return beanDefinition;

    }

    @Override
    public Properties getConfig() {
        return config;
    }

    @Override
    public List<BeanDefinition>  loadBeanDefinitions(String... locations) {
        return null;
    }
}
