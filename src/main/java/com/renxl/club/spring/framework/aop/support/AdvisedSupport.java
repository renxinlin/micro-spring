package com.renxl.club.spring.framework.aop.support;
import java.util.*;
import	java.util.stream.Collectors;

import com.renxl.club.spring.framework.aop.annotation.*;
import com.renxl.club.spring.framework.aop.aspect.*;
import com.renxl.club.spring.framework.aop.config.AopConfig;
import com.renxl.club.spring.framework.aop.config.AopConfigHolder;
import com.renxl.club.spring.framework.util.StringUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 封装了拦截器链   单例对象信息
 * 相关aop切面信息
 * 为aopproxy提供封装对象，支持代理类创建
 *
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
    private transient Map<Method, List<Advice>> methodAndMethodInterceptors;


    private transient Map<String, List<Advice>> pointCutAndMethodInterceptors;
    private transient Map<String, String> pointcutAndMethod;
    private transient Map<String, String> methodAndPointcut;

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
                    if (annotationName.equals(annotationStr)) {
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
    private void parse() throws Exception {
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

            // 织入
            Method[] targetMethods = targetClass.getMethods();
            Method[] methods = aspectClass.getMethods();
            for (Method targetMethod : targetMethods) {
                Object instance = aspectClass.newInstance();
                aspectResolver(methods, instance);
                weaver(targetMethod);
                order();


            }
            // 切人点有两种,一种是包名，一种是自定义注解
            // 通知


        }


    }

    private void order() {
        Map<Method, List<Advice>> sortedMethodAndMethodInterceptors = new HashMap  ();

        for(Map.Entry<Method, List<Advice>> methodListEntry:this.methodAndMethodInterceptors.entrySet()){
            List<Advice> interceptors = methodListEntry.getValue();
            Comparator<Advice> comparator = new Comparator<Advice>() {

                @Override
                public int compare(Advice o1, Advice o2) {
                    return o1.getOrder() - o2.getOrder();
                }
            };
            List<Advice> beforeAdvices = interceptors.stream().filter(interceptor -> interceptor instanceof BeforeAdvice).collect(Collectors.toList());
            List<Advice> aroundAdvices = interceptors.stream().filter(interceptor -> interceptor instanceof AroundAdvice).collect(Collectors.toList());
            List<Advice> afterReturnAdvices = interceptors.stream().filter(interceptor -> interceptor instanceof AfterReturnAdvice).collect(Collectors.toList());
            List<Advice> afterThrowingAdvices = interceptors.stream().filter(interceptor -> interceptor instanceof AfterThrowingAdvice).collect(Collectors.toList());
            List<Advice> afterAdvices = interceptors.stream().filter(interceptor -> interceptor instanceof AfterAdvice).collect(Collectors.toList());

            beforeAdvices.sort(comparator);
            aroundAdvices.sort(comparator);
            afterReturnAdvices.sort(comparator);
            afterThrowingAdvices.sort(comparator);
            afterAdvices.sort(comparator);

            List<Advice> sortAdvices = new LinkedList<> ();
            sortAdvices.addAll(beforeAdvices);
            sortAdvices.addAll(aroundAdvices);
            sortAdvices.addAll(afterReturnAdvices);
            sortAdvices.addAll(afterThrowingAdvices);
            sortAdvices.addAll(afterAdvices);
            methodAndMethodInterceptors.put(methodListEntry.getKey(),sortAdvices);

        }
        methodAndMethodInterceptors = sortedMethodAndMethodInterceptors;
    }

    /**
     * 织入
     * @param targetMethod
     */
    private void weaver(Method targetMethod) {
        // 切人点有两种
        // @package(com.renxl)
        List<String> packages = pointCuts.stream().filter(pointcut -> pointcut.startsWith(PRACKGE_EXPRE)).collect(Collectors.toList());

        // @annotation(com.renxl.Aop)
        List<String> annotations = pointCuts.stream().filter(pointcut -> pointcut.startsWith(ANNOTATION_EXPRE)).collect(Collectors.toList());
        if(packages!=null && packages.size()>0){
            for(String package_:packages){
                package_ = StringUtil.getSubString(package_, ANNOTATION_LEFT, ANNOTATION_RIGHT);
                String targetPackage = targetMethod.getDeclaringClass().getPackage().getName();
                if(targetPackage.equals(package_)){
                    List<Advice> methodInterceptors = methodAndMethodInterceptors.get(targetMethod);
                    if(methodInterceptors == null){
                        methodInterceptors = new ArrayList<Advice> ();
                    }
                    List<Advice> interceptors = pointCutAndMethodInterceptors.get(pointcutAndMethod.get(package_));
                    methodInterceptors.addAll(interceptors);
                    methodAndMethodInterceptors.put(targetMethod,methodInterceptors);

                }
            }
        }


        if(annotations!=null && annotations.size()>0){
            for(String annotation:annotations){
                annotation = StringUtil.getSubString(annotation, ANNOTATION_LEFT, ANNOTATION_RIGHT);
                Annotation[] declaredAnnotations = targetMethod.getDeclaredAnnotations();
                for(Annotation declaredAnnotation:declaredAnnotations){
                    // 判断是不是正常的
                    String annotationName = declaredAnnotation.getClass().getName();
                    if(annotationName.equals(annotation)){
                        List<Advice> methodInterceptors = methodAndMethodInterceptors.get(targetMethod);
                        if(methodInterceptors == null){
                            methodInterceptors = new ArrayList ();
                        }
                        List<Advice> interceptors = pointCutAndMethodInterceptors.get(pointcutAndMethod.get(annotation));
                        methodInterceptors.addAll(interceptors);
                        methodAndMethodInterceptors.put(targetMethod,methodInterceptors);

                    }

                }

            }
        }



    }

    private void aspectResolver(Method[] methods, Object instance) {
        for (Method method : methods) {
            String pointCutMethodName = "";
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
                advice = new AfterAdvice(method, instance,after.priority());

                pointCutMethodName = after.value();

            }
            if (afterReturn != null) {
                existAdviceMethod = true;
                advice = new AfterReturnAdvice(method, instance,afterReturn.priority());
                pointCutMethodName = afterReturn.value();

            }
            if (afterThrowing != null) {
                existAdviceMethod = true;
                advice = new AfterThrowingAdvice(method, instance,afterThrowing.priority());
                pointCutMethodName = afterThrowing.value();
            }
            if (around != null) {
                existAdviceMethod = true;
                advice = new AroundAdvice(method, instance,around.priority());
                pointCutMethodName = around.value();
            }
            if (before != null) {
                existAdviceMethod = true;
                advice = new BeforeAdvice(method, instance,before.priority());
                pointCutMethodName = before.value();
            }

            if (existAdviceMethod) {
                List<Advice> pointCutAndMethodInterceptor = pointCutAndMethodInterceptors.get(pointCutMethodName);
                if (pointCutAndMethodInterceptor == null) {
                    pointCutAndMethodInterceptor = new ArrayList<Advice>();
                }
                pointCutAndMethodInterceptor.add(advice);
                pointCutAndMethodInterceptors.put(pointCutMethodName, pointCutAndMethodInterceptor);
            }
            if (pointcut != null) {
                String value = pointcut.value();
                pointCuts.add(value);
                pointcutAndMethod.put(value,method.getName());
                methodAndPointcut.put(method.getName(),value);
            }
        }
    }


    /**
     * todo  重新处理
     *
     * @param method      被代理类方法 不是切面的方法
     * @param targetClass
     * @return
     * @throws NoSuchMethodException
     */
    public List<Advice> getAllAdvices(Method method, Class<?> targetClass) throws NoSuchMethodException {
        List<Advice> interceptors = methodAndMethodInterceptors.get(method);
        if (interceptors == null) {
            Method m = targetClass.getMethod(method.getName(), method.getParameterTypes());
            interceptors = methodAndMethodInterceptors.get(m);
            this.methodAndMethodInterceptors.put(m, interceptors);
        }
        return interceptors;
    }


    public Class getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(Class targetClass) throws Exception {
        this.targetClass = targetClass;
        parse();
    }


    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }


    public void setConfigHolder(AopConfigHolder configHolder) {
        this.configHolder = configHolder;
    }

}
