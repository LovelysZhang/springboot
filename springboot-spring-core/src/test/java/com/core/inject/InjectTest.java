package com.core.inject;

import com.core.BaseTest;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试Spring注入
 *
 * @author lovely
 * on 2021/11/9
 */
public class InjectTest extends BaseTest {

    @Resource
    List<BaseInvoker> list;

    @Test
    public void test() {
        System.out.println(list);
        list.forEach(BaseInvoker::doSomething);
    }
}
