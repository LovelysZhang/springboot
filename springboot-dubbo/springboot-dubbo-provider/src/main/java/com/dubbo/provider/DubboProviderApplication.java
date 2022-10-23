package com.dubbo.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.CountDownLatch;

/**
 * @author lovely
 * on 2020/2/13
 */
@SpringBootApplication
public class DubboProviderApplication {
    public static void main(String[] args) throws Exception {
        new EmbeddedZooKeeper(2181, false).start();

        SpringApplication.run(DubboProviderApplication.class, args);
        System.out.println("dubbo service started");
        new CountDownLatch(1).await();
    }
}
