package com.renxl.club.spring.framework.aop.config;

import java.util.ArrayList;

/**
 * @Author renxl
 * @Date 2020-04-22 19:39
 * @Version 1.0.0
 */
public class AopConfig {

    private String pointcut;


    private String aspectClass;

    private String after;

    private String afterReturning;

    private String afterThrowing;

    private String around;

    private String before;


    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public String getAspectClass() {
        return aspectClass;
    }

    public void setAspectClass(String aspectClass) {
        this.aspectClass = aspectClass;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getAfterReturning() {
        return afterReturning;
    }

    public void setAfterReturning(String afterReturning) {
        this.afterReturning = afterReturning;
    }

    public String getAfterThrowing() {
        return afterThrowing;
    }

    public void setAfterThrowing(String afterThrowing) {
        this.afterThrowing = afterThrowing;
    }

    public String getAround() {
        return around;
    }

    public void setAround(String around) {
        this.around = around;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }
}
