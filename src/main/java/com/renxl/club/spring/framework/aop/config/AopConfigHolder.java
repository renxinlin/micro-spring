package com.renxl.club.spring.framework.aop.config;
import	java.util.ArrayList;
import java.util.List;

/**
 *
 * 该对象不可以复用，否则容易出现
 * 多个AopConfigHolder之间的aopconfig相互干扰
 * @Author renxl
 * @Date 2020-04-22 19:39
 * @Version 1.0.0
 */
public class AopConfigHolder {
    /**
     * 切面元信息holder
     */
    private List<AopConfig> aopConfigs = new ArrayList ();

    public List<AopConfig> getAopConfigs() {
        return aopConfigs;
    }

    public void setAopConfigs(List<AopConfig> aopConfigs) {
        this.aopConfigs = aopConfigs;
    }


    public void addAopConfig(AopConfig aopConfig){
        aopConfigs.add(aopConfig);
    }
}
