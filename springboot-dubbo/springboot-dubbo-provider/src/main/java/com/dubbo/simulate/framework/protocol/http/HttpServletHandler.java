package com.dubbo.simulate.framework.protocol.http;

import com.alibaba.fastjson.JSONObject;
import com.dubbo.simulate.framework.Invocation;
import com.dubbo.simulate.framework.register.LocalRegister;
import org.apache.dubbo.common.utils.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Date: 2022/10/24
 */
public class HttpServletHandler {

    public void handler(HttpServletRequest req, HttpServletResponse resp) {
        /**
         * 处理请求
         * 分析：想一想请求里面有什么东西呢
         * 确定一个唯一的方法
         * 1. 接口名
         * 2. 方法名
         * 3. 方法参数类型列表
         * 4. 方法参数值列表
         */
        try {
            Invocation invocation = JSONObject.parseObject(req.getInputStream(), Invocation.class);
            //Invocation invocation = (Invocation) new ObjectInputStream(req.getInputStream()).readObject();

            //消费者那边存的是接口名称，不会存实现类的名称。不然会存多种多言的实现类名称。一个接口有许多实现类，改找哪个实现类呢?
            //所以需要本地注册的机制，记录接口实现类名称
            // 本地注册类：LocalRegister
            String interfaceName = invocation.getInterfaceName();
            Class clazz = LocalRegister.get(interfaceName, "1.0.0"); //默认1.0.0

            Method method = clazz.getMethod(invocation.getMethodName(), invocation.getParamType());

            Object result = method.invoke(clazz.getDeclaredConstructor().newInstance(), invocation.getParams());

            OutputStreamWriter writer = new OutputStreamWriter(resp.getOutputStream(), StandardCharsets.UTF_8);
            writer.write((String) result);
            writer.flush();
            //IOUtils.write(resp.getWriter(), (String) result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
