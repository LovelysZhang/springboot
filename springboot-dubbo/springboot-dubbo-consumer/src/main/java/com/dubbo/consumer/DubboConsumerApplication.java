package com.dubbo.consumer;


import com.dubbo.api.service.DemoService;

import org.apache.dubbo.common.stream.StreamObserver;
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

        application.doSayHelloServerStream();
        application.doSayHelloStream();
    }

    public String doSayHello(String name) {
        return demoService.sayHello(name);
    }


    public void doSayHelloServerStream() {
        demoService.sayHelloServerStream("server stream", new StreamObserver<String>() {
            @Override
            public void onNext(String s) {
                System.out.println(s);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("doSayHelloServerStream onCompleted");
            }
        });
    }

    public void doSayHelloStream() {
        //  对于 双向流(BIDIRECTIONAL_STREAM), 需要注意参数中的 StreamObserver 是响应流，返回参数中的 StreamObserver 为请求流
        StreamObserver<String> request = demoService.sayHelloStream(new StreamObserver<String>() {
            @Override
            public void onNext(String data) {
                System.out.println(data);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("doSayHelloStream onCompleted");
            }
        });
        for (int i = 0; i < 10; i++) {
            request.onNext("stream request" + i);
        }
        request.onCompleted(); //表示结束请求
    }
}