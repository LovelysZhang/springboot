package com.dubbo.simulate.framework;

import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * @Date: 2022/10/24
 */
@Getter
public class Invocation implements Serializable {

    private String interfaceName;

    private String methodName;

    private Object[] params;

    private Class[] paramType;

    public Invocation(String interfaceName, String methodName, Object[] params, Class[] paramType) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.params = params;
        this.paramType = paramType;
    }
}
