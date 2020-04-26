package sample.com.renxl.service2;

import com.renxl.club.spring.framework.annotation.Service;
import sample.com.renxl.aop.Aop;

/**
 * @Author renxl
 * @Date 2020-04-25 18:23
 * @Version 1.0.0
 */
@Service
public class ServiceF {


    @Aop
    public void eat() {
        System.out.println("im f... ");
    }
}
