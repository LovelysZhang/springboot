package com.dubbo.simulate.provider;

import com.dubbo.simulate.framework.protocol.http.HttpServer;
import com.dubbo.simulate.framework.register.LocalRegister;
import com.dubbo.simulate.provider.api.HelloServiceImpl;
import com.dubbo.simulate.provider.api.HelloService;

/**
 * @Date: 2022/10/24
 */
public class MyDubboProvider {


    public static void main(String[] args) {
        LocalRegister.register(HelloService.class.getName(), "1.0.0", HelloServiceImpl.class);

        // 可以使用多种框架实现 tomcat/Jetty Http 请求、netty、mina
        HttpServer server = new HttpServer();
        server.start("localhost", 8080);
    }
}
