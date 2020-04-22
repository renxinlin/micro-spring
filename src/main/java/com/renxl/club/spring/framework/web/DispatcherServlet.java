package com.renxl.club.spring.framework.web;
import	java.util.logging.Logger;

import com.renxl.club.spring.framework.annotation.Controller;
import com.renxl.club.spring.framework.annotation.RequestMapping;
import com.renxl.club.spring.framework.bean.BeanDefinition;
import com.renxl.club.spring.framework.bean.support.BeanDefinitionReader;
import com.renxl.club.spring.framework.bean.support.DefaultListableBeanFactory;
import com.renxl.club.spring.framework.context.DefaultApplicationContext;
import com.renxl.club.spring.framework.context.support.ApplicationContext;
import com.renxl.club.spring.framework.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 这些方法都是servlet规范下的产生的
 * Tomcat也要遵循servlet规范
 *
 * @Author renxl
 * @Date 2020-04-20 19:04
 * @Version 1.0.0
 */

public class DispatcherServlet extends HttpServlet {
    private static final String CONTEXT_CONFIG_LOCATION = "contextConfigLocation";

    // 在tomcat+ssm源码中applicationContext在dispatchservl存在
    // 同时mvc容器和spring容器都存在于 tomcat context的config的attributes中

    private ApplicationContext applicationContext;

    // 将上下文全部保存到dispatchservlet中
    private List<HandlerMapping> handlerMappings = new ArrayList();

    private Map<HandlerMapping, HandlerAdapter> handlerAdapters = new HashMap();

    private List<ViewResolver> viewResolvers = new ArrayList();
    private String webRootDir = "webRootDir";


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            this.doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("server error : " + " _ error by renxl");

        }
    }


    private void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //
        String url = req.getRequestURI();
        String contextPath = req.getContextPath();
        url = url.replace(contextPath, "").replaceAll("/+", "/");
        String finalUrl = url;
        HandlerMapping handlerMapping = null;

        for (HandlerMapping handler:handlerMappings){
            Matcher matcher = handler.getPattern().matcher(finalUrl);
            if(matcher.matches()){
                handlerMapping = handler;
                break;
            }
        };


        if(handlerMapping == null){
            return404(req,resp);
            return;
        }
        HandlerAdapter handlerAdapter = handlerAdapters.get(handlerMapping);
        if(!(handlerAdapter!=null && handlerAdapter.supports(handlerMapping))){
            return404(req,resp);
            return;


        }
        ModelAndView mv = handlerAdapter.handle(req, resp, handlerMapping);
        processResult(req, resp, mv);

    }

    private void processResult(HttpServletRequest req, HttpServletResponse resp, ModelAndView mv) throws Exception {
        if(mv ==null){
            return404(req,resp);
        }
        if(mv.isJson()){
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("application/json");
            Object data = mv.getData();
            String json = JsonUtil.toJson(data);
            resp.getWriter().write(json);
            return;
        }
        //如果ModelAndView不为null，怎么办？
        if(this.viewResolvers.isEmpty()){return;}

        for (ViewResolver viewResolver : this.viewResolvers) {
            View view = viewResolver.resolveViewName(mv.getViewName(),null);
            view.render(mv.getModel(),req,resp);
            return;
        }
    }

    private void return404(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");
        String json = "its 404,  by renxl";
        resp.getWriter().write(json);
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        // spring 源码中这里有9大核心组件，我们这里只实现最重要的handlerMapping+handler+ handlerAdapter
        // 关于拦截器 ，其和aop的抽象层设计思想基本一致,为链式弹栈调用
        //1、初始化ApplicationContext
        applicationContext = new DefaultApplicationContext(config.getInitParameter(CONTEXT_CONFIG_LOCATION));
        //2、初始化Spring MVC 九大组件
        initStrategies(applicationContext);

        System.out.println("============spring and mvc start over by renxl");
    }

    private void initStrategies(ApplicationContext applicationContext) {

        //handlerMapping，必须实现
        initHandlerMappings(applicationContext);
        //初始化参数适配器，必须实现
        initHandlerAdapters(applicationContext);
        //初始化异常拦截器
        initHandlerExceptionResolvers(applicationContext);
        //初始化视图转换器，必须实现
        initViewResolvers(applicationContext);
    }

    private void initViewResolvers(ApplicationContext applicationContext) {
//拿到模板的存放目录
        BeanDefinitionReader reader = ((DefaultApplicationContext) applicationContext).getReader();
        String fileRoot = reader.getConfig().getProperty(webRootDir);
        String templateRootPath = this.getClass().getClassLoader().getResource(fileRoot).getFile();

        File templateRootDir = new File(templateRootPath);
        String[] templates = templateRootDir.list();
        for (int i = 0; i < templates.length; i++) {
            //这里主要是为了兼容多模板，所有模仿Spring用List保存
            //在我写的代码中简化了，其实只有需要一个模板就可以搞定
            //只是为了仿真，所有还是搞了个List
            this.viewResolvers.add(new ViewResolver(fileRoot));
        }
    }

    private void initHandlerExceptionResolvers(ApplicationContext applicationContext) {
        // todo
    }

    private void initHandlerAdapters(ApplicationContext applicationContext) {
        // 我们这里不是很规范，所有的handler都对应了一个adapter
        for (HandlerMapping handlerMapping : this.handlerMappings) {
            this.handlerAdapters.put(handlerMapping, new HandlerAdapter());
        }

    }

    private void initHandlerMappings(ApplicationContext applicationContext) {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) applicationContext;
        Collection<BeanDefinition> beanDefinitions = defaultListableBeanFactory.getBeanDefinitionMap().values();
        beanDefinitions.forEach(bd -> {
            Object bean = defaultListableBeanFactory.getBean(bd.getBeanName());
            try {
                Class<?> clazz = bean.getClass();
                if (clazz.isAnnotationPresent(Controller.class)) {
                    String baseUrl = "";
                    if (clazz.isAnnotationPresent(RequestMapping.class)) {
                        RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
                        baseUrl = requestMapping.value();
                    }

                    Method[] methods = clazz.getMethods();
                    for (Method method : methods) {
                        if (!method.isAnnotationPresent(RequestMapping.class)) {
                            continue;
                        }
                        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                        String regex = ("/" + baseUrl + "/" + requestMapping.value().replaceAll("\\*", ".*")).replaceAll("/+", "/");
                        Pattern pattern = Pattern.compile(regex);
                        this.handlerMappings.add(new HandlerMapping(pattern, bean, method));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


}
