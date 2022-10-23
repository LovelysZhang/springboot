package com.dubbo.consumer;


import com.dubbo.api.service.DemoService;

import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @author lovely
 * on 2020/2/13
 */
@SpringBootApplication
@Service
@EnableDubbo
public class DubboConsumerApplication {

    @DubboReference
    private DemoService demoService;

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(DubboConsumerApplication.class, args);
        DubboConsumerApplication application = context.getBean(DubboConsumerApplication.class);
        String result = application.doSayHello("world");
        System.out.println("result: " + result);
    }

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }
}
