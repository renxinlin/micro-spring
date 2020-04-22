package com.renxl.club.spring.framework.web;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @Author renxl
 * @Date 2020-04-20 19:27
 * @Version 1.0.0
 */
public class HandlerMapping {

    private Object controller;	//保存方法对应的实例
    private Method method;		//保存映射的方法
    private Pattern pattern;    //URL的正则匹配

    public HandlerMapping(Pattern pattern,Object controller, Method method) {
        this.controller = controller;
        this.method = method;
        this.pattern = pattern;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }



}
