package com.dubbo.simulate.consumer;

import com.dubbo.simulate.framework.proxy.ProxyFactory;
import com.dubbo.simulate.provider.api.HelloService;

/**
 * @Date: 2022/10/24
 */
public class MyDubboConsumer {

    public static void main(String[] args) {


        //Invocation invocation = new Invocation(HelloService.class.getName(), "sayHello", new Object[]{"hello world"}, new Class[]{String.class});
        //HttpClient client = new HttpClient();
        //String result = client.send("localhost", 8080, invocation);
        //System.out.println(result);

        //使用动态代理简化该段代码逻辑
        HelloService helloService = (HelloService) ProxyFactory.getProxy(HelloService.class);
        System.out.println(helloService.sayHello("动态代理"));
    }
}
