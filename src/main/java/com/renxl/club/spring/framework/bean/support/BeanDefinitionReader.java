package com.renxl.club.spring.framework.bean.support;


/**
 *  location就是需要扫描的路径
 */
public interface BeanDefinitionReader {

	int loadBeanDefinitions(String location);


	int loadBeanDefinitions(String... locations);

}
