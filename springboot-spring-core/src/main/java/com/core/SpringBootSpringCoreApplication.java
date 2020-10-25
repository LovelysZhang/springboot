package com.core;

import com.core.config.MyWebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @Configuration:指明当前类是一个配置类，用来代替@ImportResource的注解和Spring的配置文件
 *
 *
 * @author non human
 * on 2020/3/19
 */
@SpringBootApplication
//@Import(MyWebConfig.class)
public class SpringBootSpringCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootSpringCoreApplication.class);
    }
}