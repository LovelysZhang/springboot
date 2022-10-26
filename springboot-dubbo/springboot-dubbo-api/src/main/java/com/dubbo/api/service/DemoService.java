package com.dubbo.api.service;

import org.apache.dubbo.common.stream.StreamObserver;

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

    /**
     * 双向流
     * @param response
     * @return
     */
    StreamObserver<String> sayHelloStream(StreamObserver<String> response);

    /**
     * 服务端流
     * @param request
     * @param response
     */
    void sayHelloServerStream(String request, StreamObserver<String> response);
}
