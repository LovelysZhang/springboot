package com.dubbo.simulate.framework.register;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 远程服务注册中心
 * 作用：共享 消费者 、生产者的信息
 *
 * @Date: 2022/10/25
 */
public class RemoteRegister {

    // redis：发布订阅机制
    // zk：
    private static final Map<String, List<URL>> REGISTER = new HashMap<>();


    public static void register(String interfaceName, URL url) {
        List<URL> list = REGISTER.get(interfaceName);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(url);

        REGISTER.put(interfaceName, list);
    }

    public static List<URL> get(String interfaceName) {
        return REGISTER.get(interfaceName);
    }

}
