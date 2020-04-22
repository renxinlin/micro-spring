package com.renxl.club.spring.framework.web;

import com.renxl.club.spring.framework.annotation.RequestParam;
import com.renxl.club.spring.framework.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author renxl
 * @Date 2020-04-20 19:28
 * @Version 1.0.0
 */
public class HandlerAdapter {
    public boolean supports(Object handler){
        // 默认全是HttpRequestHandler
        return (handler instanceof HandlerMapping) && (true);
    }

    /**
     * 这段方法只做了一件事，让controller正确执行返回
     * 拆分为 让request的参数和controller的参数对应上
     * 返回MV和json两种格式
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        // 以下都是在处理参数
        HandlerMapping handlerMapping = (HandlerMapping)handler;
        Map<String,Integer> paramIndexMapping = new HashMap<String, Integer>();
        Annotation[] [] pa = handlerMapping.getMethod().getParameterAnnotations();
        for (int i = 0; i < pa.length ; i ++) {
            for(Annotation a : pa[i]){
                if(a instanceof RequestParam){
                    String paramName = ((RequestParam) a).value();
                    if(!"".equals(paramName.trim())){
                        paramIndexMapping.put(paramName, i);
                    }
                }
            }
        }

        Class<?> [] paramsTypes = handlerMapping.getMethod().getParameterTypes();
        for (int i = 0; i < paramsTypes.length ; i ++) {
            Class<?> type = paramsTypes[i];
            if(type == HttpServletRequest.class ||
                    type == HttpServletResponse.class){
                paramIndexMapping.put(type.getName(),i);
            }
        }

        Map<String,String[]> params = request.getParameterMap();

        Object [] paramValues = new Object[paramsTypes.length];

        for (Map.Entry<String, String[]> parm : params.entrySet()) {
            String value = Arrays.toString(parm.getValue()).replaceAll("\\[|\\]","")
                    .replaceAll("\\s",",");

            if(!paramIndexMapping.containsKey(parm.getKey())){continue;}

            int index = paramIndexMapping.get(parm.getKey());
            paramValues[index] = caseStringValue(value,paramsTypes[index]);
        }

        if(paramIndexMapping.containsKey(HttpServletRequest.class.getName())) {
            int reqIndex = paramIndexMapping.get(HttpServletRequest.class.getName());
            paramValues[reqIndex] = request;
        }

        if(paramIndexMapping.containsKey(HttpServletResponse.class.getName())) {
            int respIndex = paramIndexMapping.get(HttpServletResponse.class.getName());
            paramValues[respIndex] = response;
        }


        // 以下为 执行
        Object result = handlerMapping.getMethod().invoke(handlerMapping.getController(),paramValues);
        if(result == null || result instanceof Void){ return null; }


        boolean isModelAndView = handlerMapping.getMethod().getReturnType() == ModelAndView.class;
        if(isModelAndView){
            return (ModelAndView) result;
        }else {
            // 处理responsebody注解
            Annotation annotation = handlerMapping.getMethod().getAnnotation(ResponseBody.class);
            if(annotation != null){
                return new ModelAndView(true,result);
            }else {
                return null;
            }

        }

    }

    private Object caseStringValue(String value, Class<?> paramsType) {
        if(String.class == paramsType){
            return value;
        }
        //如果是int
        if(Integer.class == paramsType){
            return Integer.valueOf(value);
        }
        else if(Double.class == paramsType){
            return Double.valueOf(value);
        }else {
            if(value != null){
                return value;
            }
            return null;
        }
        //如果还有double或者其他类型，继续加if
        //这时候，我们应该想到策略模式了
        //在这里暂时不实现，希望小伙伴自己来实现

    }
}
