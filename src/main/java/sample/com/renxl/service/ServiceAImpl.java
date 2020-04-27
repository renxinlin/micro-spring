package sample.com.renxl.service;

import com.renxl.club.spring.framework.annotation.Autowired;
import com.renxl.club.spring.framework.annotation.Service;
import sample.com.renxl.service.ServiceA;
import sample.com.renxl.service.ServiceC;

/**
 * @Author renxl
 * @Date 2020-04-25 18:21
 * @Version 1.0.0
 */
@Service
public class ServiceAImpl implements ServiceA {


    @Autowired
    private ServiceC servicec;
    @Override
    public String say() {
//        servicec.eat();
        return "say";
    }
}
