package com.dubbo.simulate.provider.api;

/**
 * @Date: 2022/10/24
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String s) {
        return "provider=>" + s;
    }
}
