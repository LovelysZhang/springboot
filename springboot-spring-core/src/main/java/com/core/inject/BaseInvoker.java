package com.core.inject;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lovely
 * on 2021/11/9
 */
public abstract class BaseInvoker {


    @Resource
    List<BaseInvoker> list;


    void doSomething() {
        list.forEach((k) -> System.out.println("--->" + k));
    }
}
