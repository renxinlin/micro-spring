package com.renxl.club.spring.framework.aop.support;

import com.renxl.club.spring.framework.aop.annotation.*;
import com.renxl.club.spring.framework.aop.aspect.*;
import com.renxl.club.spring.framework.aop.config.AopConfig;
import com.renxl.club.spring.framework.aop.config.AopConfigHolder;
import com.renxl.club.spring.framework.util.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 *
 *
 * 封装了拦截器链   单例对象信息
 * 相关aop切面信息
 * 为aopproxy提供封装对象，支持代理类创建
 * @Author renxl
 * @Date 2020-04-22 20:01
 * @Version 1.0.0
 */
public class AdvisedSupport {
    private static final String PRACKGE_EXPRE = "@package(";
    private static final String ANNOTATION_EXPRE = "@annotation(";
    private static final String ANNOTATION_LEFT = "(";
    private static final String ANNOTATION_RIGHT = ")";

    /**
     * 连接点所属对象的目标类
     */
    private Class targetClass;


    /**
     * 切入点规则集合 ：目前实现包切人   注解切人
     * 不支持切人点表达式 //todo 研究下切人点的正则
     */
    private List<String> pointCuts;


    /**
     * 连接点所属对象
     */
    private Object target;
    /**
     * 封装切面信息
     */
    private AopConfigHolder configHolder;

    /**
     * method和method对应的拦截器链路
     */
    private transient Map<Method, List<Object>> methodAndMethodInterceptors;

    public boolean pointCutMatch() throws ClassNotFoundException {


        if (pointCuts == null) {
            return false;
        }
        List<String> annotations = new ArrayList<String>();
        String packageName = targetClass.getPackage().getName();
        for (String expression : pointCuts) {
            if (expression.startsWith(PRACKGE_EXPRE)) {
                return StringUtil.getSubString(expression, ANNOTATION_LEFT, ANNOTATION_RIGHT).equals(packageName);
            }

            if (expression.startsWith(ANNOTATION_EXPRE)) {
                annotations.add(StringUtil.getSubString(expression, ANNOTATION_LEFT, ANNOTATION_RIGHT));
            }
        }

        if (annotations.size() == 0) {
            return false;
        }

        // 取交集后获取公有方法的注解，不对私有方法进行aop
        Method[] methods = targetClass.getMethods();
        for (Method method : methods) {
            Annotation[] annotationsOnMethods = method.getDeclaredAnnotations();
            if (annotationsOnMethods == null || annotationsOnMethods.length == 0) {
                continue;
            }
            for (Annotation annotationsOnMethod : annotationsOnMethods) {
                String annotationName = annotationsOnMethod.annotationType().getDeclaringClass().getName();
                for (String annotationStr : annotations) {
                    if(annotationName.equals(annotationStr)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 解析拦截器链
     */
    private void parse() throws Exception{
        /**
         * 每一个aopconfig代表一个含@aspect注解的切面
         * */
        List<AopConfig> aopConfigs = configHolder.getAopConfigs();

        for (AopConfig aopConfig : aopConfigs) {
            String aspectClassStr = aopConfig.getAspectClass();
            Class aspectClass = Class.forName(aspectClassStr);
            /**
             *
             * 三种类型的方法
             * 通知
             * 切人点:
             *       这里我们只支持基于注解的切入点@annotation(com.renxl.Logger)
             *       以及基于指定包的切人点  "@package(com.renxl.club)"
             * 普通
             *
             * 获取所有的方法
             */
            Method[] methods = aspectClass.getMethods();
            // 切人点有两种,一种是包名，一种是自定义注解
            // 通知
            Object instance = aspectClass.newInstance();
            for (Method method : methods) {
                After after = (After) method.getAnnotation(After.class);
                AfterReturning afterReturn = method.getAnnotation(AfterReturning.class);
                AfterThrowing afterThrowing = method.getAnnotation(AfterThrowing.class);
                Around around = method.getAnnotation(Around.class);
                Before before = method.getAnnotation(Before.class);
                Pointcut pointcut = method.getAnnotation(Pointcut.class);
                boolean existAdviceMethod = false;
                Advice advice = null;
                if (after != null) {
                    existAdviceMethod = true;
                    advice = new AfterAdvice(method, instance);

                }
                if (afterReturn != null) {
                    existAdviceMethod = true;
                    advice = new AfterReturnAdvice(method, instance);

                }
                if (afterThrowing != null) {
                    existAdviceMethod = true;
                    advice = new AfterThrowingAdvice(method, instance);
                }
                if (around != null) {
                    existAdviceMethod = true;
                    advice = new AroundAdvice(method, instance);
                }
                if (before != null) {
                    existAdviceMethod = true;
                    advice = new BeforeAdvice(method, instance);
                }

                if (existAdviceMethod) {
                    List<Object> methodInterceptor = methodAndMethodInterceptors.get(method);
                    if (methodInterceptor == null) {
                        methodInterceptor = new ArrayList<Object>();
                    }
                    methodInterceptor.add(advice);
                }
                if (pointcut != null) {
                    String value = pointcut.value();
                    pointCuts.add(value);
                }


            }

        }


    }



    /**
     * todo  重新处理
     * @param method
     * @param targetClass
     * @return
     * @throws NoSuchMethodException
     */
    public List<Object> getAllAdvices(Method method, Class<?> targetClass) throws NoSuchMethodException {
        List<Object> interceptors = methodAndMethodInterceptors.get(method);
        if(interceptors == null){
            Method m = targetClass.getMethod(method.getName(),method.getParameterTypes());
            interceptors = methodAndMethodInterceptors.get(m);
            this.methodAndMethodInterceptors.put(m,interceptors);
        }
        return interceptors;
    }



    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) {
        this.targetClass = targetClass;
    }


    public List<String> getPointCuts() {
        return pointCuts;
    }

    public void setPointCuts(List<String> pointCuts) {
        this.pointCuts = pointCuts;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public AopConfigHolder getConfigHolder() {
        return configHolder;
    }

    public void setConfigHolder(AopConfigHolder configHolder) {
        this.configHolder = configHolder;
    }

    public Map<Method, List<Object>> getMethodAndMethodInterceptors() {
        return methodAndMethodInterceptors;
    }

    public void setMethodAndMethodInterceptors(Map<Method, List<Object>> methodAndMethodInterceptors) {
        this.methodAndMethodInterceptors = methodAndMethodInterceptors;
    }
}
