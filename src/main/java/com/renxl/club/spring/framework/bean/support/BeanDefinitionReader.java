package com.renxl.club.spring.framework.bean.support;


import com.renxl.club.spring.framework.bean.BeanDefinition;

import java.util.List;
import java.util.Properties;

/**
 *  location就是需要扫描的路径
 */
public interface BeanDefinitionReader {

	List<BeanDefinition> loadBeanDefinitions();


	List<BeanDefinition>  loadBeanDefinitions(String... locations);


	Properties getConfig();

}
