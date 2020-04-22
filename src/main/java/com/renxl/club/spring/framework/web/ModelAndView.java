package com.renxl.club.spring.framework.web;

import java.util.Map;

/**
 * @Author renxl
 * @Date 2020-04-20 19:28
 * @Version 1.0.0
 */
public class ModelAndView {
    private String viewName;
    private Map<String,String> model;


    private Object data;
    private boolean isJson = false;

    public boolean isJson() {
        return isJson;
    }

    public Object getData() {
        return data;
    }

    public ModelAndView(boolean isJson, Object data) {
        this.isJson = true;
        this.data = data;
    }

    public ModelAndView(String viewName) { this.viewName = viewName; }

    public ModelAndView(String viewName, Map<String, String> model) {
        this.viewName = viewName;
        this.model = model;
    }

    public String getViewName() {
        return viewName;
    }


    public Map<String, String> getModel() {
        return model;
    }
}
