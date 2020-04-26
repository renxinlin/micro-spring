package sample.com.renxl.service2;

import com.renxl.club.spring.framework.annotation.Service;
import sample.com.renxl.aop.Aop;

/**
 * @Author renxl
 * @Date 2020-04-25 18:20
 * @Version 1.0.0
 */
@Service
public class ServiceE {


    @Aop
    public String shuo(){
        return "servicee";
    }
}
