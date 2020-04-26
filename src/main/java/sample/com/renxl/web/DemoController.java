package sample.com.renxl.web;
import	java.util.HashMap;

import com.renxl.club.spring.framework.annotation.*;
import com.renxl.club.spring.framework.web.ModelAndView;
import sample.com.renxl.service.ServiceA;
import sample.com.renxl.service.ServiceB;
import sample.com.renxl.service.ServiceC;
import sample.com.renxl.service2.ServiceD;
import sample.com.renxl.service2.ServiceE;
import sample.com.renxl.service2.ServiceF;

import java.util.Map;

/**
 * @Author renxl
 * @Date 2020-04-16 15:53
 * @Version 1.0.0
 */
@Controller
@RequestMapping("/hi")
public class DemoController {
    @Autowired
    private ServiceA service;

    @Autowired
    private ServiceB serviceb;

    @Autowired
    private ServiceC servicec;
    @Autowired
    private ServiceD serviced;
    @Autowired
    private ServiceE servicee;
    @Autowired
    private ServiceF servicef;

    @RequestMapping("/service")
    @ResponseBody
    public Map<String, String>  service(@RequestParam("renxl") String renxl) {
        HashMap<String, String> map = new HashMap<>();
        service.say();
        map.put("renxl", renxl);
        return map;
    }

    @RequestMapping("/serviceb")
    @ResponseBody
    public Map<String, String>  serviceb(@RequestParam("renxl") String renxl) {
        HashMap<String, String> map = new HashMap<>();
        serviceb.shuo();
        map.put("renxl", renxl);
        return map;
    }

    @RequestMapping("/servicec")
    @ResponseBody
    public Map<String, String>  servicec(@RequestParam("renxl") String renxl) {
        HashMap<String, String> map = new HashMap<>();
        servicec.eat();
        map.put("renxl", renxl);
        return map;
    }

    @RequestMapping("/serviced")
    @ResponseBody
    public Map<String, String>  serviced(@RequestParam("renxl") String renxl) {
        HashMap<String, String> map = new HashMap<>();
        serviced.say();
        map.put("renxl", renxl);
        return map;
    }

    @RequestMapping("/servicee")
    @ResponseBody
    public Map<String, String>  servicee(@RequestParam("renxl") String renxl) {
        HashMap<String, String> map = new HashMap<>();
        servicee.shuo();
        map.put("renxl", renxl);
        return map;
    }

    @RequestMapping("/servicef")
    @ResponseBody
    public Map<String, String>  servicef(@RequestParam("renxl") String renxl) {
        HashMap<String, String> map = new HashMap<>();
        servicef.eat();
        map.put("renxl", renxl);
        return map;
    }

    @RequestMapping("/spring")
    public ModelAndView hispring(@RequestParam("renxl") String renxl) {
        HashMap<String, String> model = new HashMap<>();
        service.say();
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
