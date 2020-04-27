package sample.com.renxl.service2;

import com.renxl.club.spring.framework.annotation.Autowired;
import com.renxl.club.spring.framework.annotation.Service;
import sample.com.renxl.aop.Aop;

/**
 * @Author renxl
 * @Date 2020-04-25 18:21
 * @Version 1.0.0
 */
@Service
public class ServiceDImpl implements ServiceD {


    @Autowired
    private ServiceF servicef;
    @Aop
    @Override
    public String say() {
        System.out.println("d");

        servicef.eat();
        return "dimpl";
    }
}
