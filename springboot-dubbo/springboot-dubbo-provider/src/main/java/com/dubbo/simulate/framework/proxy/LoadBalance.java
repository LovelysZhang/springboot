package com.dubbo.simulate.framework.proxy;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 负载均衡
 *
 * @Date: 2022/10/25
 */
public class LoadBalance {


    public static URL random(List<URL> list) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        int n = random.nextInt(list.size());
        return list.get(n);
    }
}
