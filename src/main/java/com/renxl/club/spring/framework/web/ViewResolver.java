package com.renxl.club.spring.framework.web;

import java.io.File;
import java.util.Locale;

/**
 * @Author renxl
 * @Date 2020-04-20 19:29
 * @Version 1.0.0
 */
public class ViewResolver {

    // 引用某名校同事的话语：一般一个模型需要被处理，命名就会是【名词+动词】$(Model)Reslover/Loader/Builder等
    private final String DEFAULT_TEMPLATE_SUFFX = ".html";

    private File staticWebRootPath;

    public ViewResolver(String staticPath) {
        String path = this.getClass().getClassLoader().getResource(staticPath).getFile();
        staticWebRootPath = new File(path);
    }

    public View resolveViewName(String viewName, Locale locale) throws Exception{
        if(null == viewName || "".equals(viewName.trim())){return null;}
        viewName = viewName.endsWith(DEFAULT_TEMPLATE_SUFFX) ? viewName : (viewName + DEFAULT_TEMPLATE_SUFFX);
        File templateFile = new File((staticWebRootPath.getPath() + "/" + viewName).replaceAll("/+","/"));
        return new View(templateFile);
    }

}
