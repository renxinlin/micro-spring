package sample.com.renxl.service;

import com.renxl.club.spring.framework.annotation.Service;

/**
 * @Author renxl
 * @Date 2020-04-25 18:20
 * @Version 1.0.0
 */
@Service
public class ServiceB {



    public String shuo(){
        System.out.println("b");
        return "shuo";
    }
}
