package com.renxl.club.spring.framework.aop.interceptor;

import com.renxl.club.spring.framework.aop.aspect.JoinPoint;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author renxl
 * @Date 2020-04-22 20:05
 * @Version 1.0.0
 *
 * 该类封装了 support的拦截器链 并完成拦截器链和连接点的正确执行
 */
public class MethodInvocation implements JoinPoint {




    public MethodInvocation(Method method, Object target, Object[] arguments, List<Object> interceptorsAndDynamicMethodMatchers, Class<?> targetClass, Map<String, Object> attributes) {
        this.method = method;
        this.target = target;
        this.arguments = arguments;
        this.interceptorsAndDynamicMethodMatchers = interceptorsAndDynamicMethodMatchers;
        this.targetClass = targetClass;
        this.attributes = attributes;
    }


    private Method method;

    private Object target;

    private Object [] arguments;

    private List<Object> interceptorsAndDynamicMethodMatchers;

    private Class<?> targetClass;


    /**
     * 数据扩展点
     */
    private Map<String, Object> attributes;;

    // 记录当前拦截器链执行位置  这个东西注意下，spring源码中也有这样一个东西
    private int currentInterceptorIndex = -1;





    @Override
    public Object getThis() {
        return this.target;
    }

    @Override
    public Object[] getArguments() {
        return this.arguments;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public void setUserAttribute(String key, Object value) {
        if (value != null) {
            if (this.attributes == null) {
                this.attributes = new HashMap<String,Object>();
            }
            this.attributes.put(key, value);
        }
        else {
            if (this.attributes != null) {
                this.attributes.remove(key);
            }
        }
    }

    @Override
    public Object getUserAttribute(String key) {
        return (this.attributes != null ? this.attributes.get(key) : null);

    }

    /**
     * 这是spring的aop顺序
     * 我们也实现成这种方式
     *
     * class com.cn.aop.demo4.Before1 Before通知 order = 1
     * class com.cn.aop.demo4.Before2 Before通知 order = 2
     * class com.cn.aop.demo4.Around1 Around通知 order = 100000 start
     * class com.cn.aop.demo4.Around2 Around通知 order = 100000 start
     * class com.cn.aop.demo4.AServiceImpl.m1()
     * class com.cn.aop.demo4.Around2 Around通知 order = 100000 end
     * class com.cn.aop.demo4.Around1 Around通知 order = 100000 end
     * class com.cn.aop.demo4.AfterReturning1 AfterReturning通知 order = 100000
     * class com.cn.aop.demo4.AfterReturning2 AfterReturning通知 order = 100000
     * class com.cn.aop.demo4.After2 After通知 order = 100000
     * class com.cn.aop.demo4.After1 After通知 order = 100000
     *
     * ========================
     *
     * class com.cn.aop.demo4.Before1 Before通知 order = 1
     * class com.cn.aop.demo4.Before2 Before通知 order = 2
     * class com.cn.aop.demo4.Around1 Around通知 order = 100000 start
     * class com.cn.aop.demo4.Around2 Around通知 order = 100000 start
     * class com.cn.aop.demo4.AfterThrowing2 AfterThrowing通知 order = 100000
     * class com.cn.aop.demo4.AfterThrowing1 AfterThrowing通知 order = 100000
     * class com.cn.aop.demo4.After2 After通知 order = 100000
     * class com.cn.aop.demo4.After1 After通知 order = 100000
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     * @return
     */
    public Object proceed() {
        // 我们将 handlerinvocation的功能抽象到这里，并采用递归调用的方式直到目标方法和拦截器链全部执行完毕
//        currentInterceptorIndex  list是按照顺序排好的

        return null;
    }
}
