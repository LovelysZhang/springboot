package com.dubbo.simulate.framework.proxy;

import com.dubbo.simulate.framework.Invocation;
import com.dubbo.simulate.framework.protocol.http.HttpClient;
import com.dubbo.simulate.framework.register.RemoteRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.List;

/**
 * @author Non
 * @Date: 2022/10/24
 */
public class ProxyFactory<T> {

    @SuppressWarnings("unchecked")
    public static <T> T getProxy(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    //伪造数据
                    String mock = System.getProperty("mock");
                    if (mock != null && mock.startsWith("return")) {
                        return mock.replace("return", "");
                    }

                    HttpClient client = new HttpClient();
                    Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(), args, method.getParameterTypes());
                    // 使用远程注册保存hostname、port等信息
                    // 还可以做负载均衡
                    //return client.send("localhost", 8080, invocation);

                    List<URL> urls = RemoteRegister.get(interfaceClass.getName());
                    URL url = LoadBalance.random(urls); // 负载均衡
                    return client.send(url.getHost(), url.getPort(), invocation);
                } catch (Exception e) {
                    System.out.println("容错处理");
                    e.printStackTrace();
                }
                return null;
            }
        });
    }
}
