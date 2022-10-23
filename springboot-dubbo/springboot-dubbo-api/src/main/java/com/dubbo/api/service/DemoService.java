package com.dubbo.api.service;

import java.util.concurrent.CompletableFuture;

/**
 * @author lovely
 * on 2020/2/13
 */
public interface DemoService {

    String sayHello(String name);

    default CompletableFuture<String> sayHelloAsync(String name) {
        return CompletableFuture.completedFuture(sayHello(name));
    }
}
