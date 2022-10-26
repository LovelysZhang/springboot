package com.dubbo.simulate.framework.register;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地注册
 *
 * @Date: 2022/10/24
 */
public class LocalRegister {

    private static Map<String, Class> map = new ConcurrentHashMap();

    /**
     * 待优化点：支持版本 version
     *
     * @param interfaceName
     * @param implClass
     */
    public static void register(String interfaceName, String version, Class implClass) {
        map.put(interfaceName + version, implClass);
    }

    public static Class get(String interfaceName, String version) {
        return map.get(interfaceName + version);
    }

}
