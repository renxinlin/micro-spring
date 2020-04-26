package com.renxl.club.spring.framework.context.support;

/**
 * @Author renxl
 * @Date 2020-04-26 13:36
 * @Version 1.0.0
 */
public class BeanName {
    private String name;
    private boolean primary;

    public BeanName() {
    }

    public BeanName(String name, boolean primary) {
        this.name = name;
        this.primary = primary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }
}
