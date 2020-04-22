package sample.com.renxl.web;
import	java.util.HashMap;

import com.renxl.club.spring.framework.annotation.Controller;
import com.renxl.club.spring.framework.annotation.RequestMapping;
import com.renxl.club.spring.framework.annotation.RequestParam;
import com.renxl.club.spring.framework.annotation.ResponseBody;
import com.renxl.club.spring.framework.web.ModelAndView;

import java.util.Map;

/**
 * @Author renxl
 * @Date 2020-04-16 15:53
 * @Version 1.0.0
 */
@Controller
@RequestMapping("/hi")
public class DemoController {

    @RequestMapping("/spring")
    public ModelAndView hispring(@RequestParam("renxl") String renxl) {
        HashMap<String, String> model = new HashMap<>();
        model.put("renxl",renxl);
        return new ModelAndView("renxl",model);
    }


    @RequestMapping("/spring1")
    public ModelAndView hispring1(@RequestParam("renxl") String renxl) {
        HashMap<String, String> model = new HashMap<>();
        model.put("renxl",renxl);
        return new ModelAndView("renxl");
    }



    @RequestMapping("/renxl")
    @ResponseBody
    public Map<String, String>  hirenxl(@RequestParam("renxl") String renxl) {
        HashMap<String, String> map = new HashMap<>();
        map.put("renxl",renxl);
        return map;
    }
}
