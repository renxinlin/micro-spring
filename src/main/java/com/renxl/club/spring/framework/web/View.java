package com.renxl.club.spring.framework.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author renxl
 * @Date 2020-04-20 19:28
 * @Version 1.0.0
 */
public class View {

    public final String DEFULAT_CONTENT_TYPE = "text/html;charset=utf-8";

    private File viewFile;

    public View(File viewFile) {
        this.viewFile = viewFile;
    }

    public void render(Map<String, String> model,
                       HttpServletRequest request, HttpServletResponse response) throws Exception{
        StringBuffer sb = new StringBuffer();
        RandomAccessFile ra = new RandomAccessFile(this.viewFile,"r");
        String line  = null;

        // replaceall为正则 replace不是
        while (null != (line = ra.readLine())){
            String finalLine = line;
            if(model!=null && model.size() > 0){
                model.entrySet().forEach(model_key_view ->{
                    String patternLine = finalLine.replaceAll("\\$\\{" + model_key_view.getKey() + "\\}",
                            model_key_view.getValue() == null ? "":model_key_view.getValue());
                    sb.append(patternLine);
                });
            }else {
                sb.append(finalLine);
            }
        }
        response.setCharacterEncoding("utf-8");
        response.setContentType(DEFULAT_CONTENT_TYPE);
        response.getWriter().write(sb.toString());

    }


}
