package com.tx;

import com.tx.repository.dao.ActivityOrderTaskDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author lovely
 * on 2021/9/23
 */
@SpringBootApplication
public class SpringTxExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTxExampleApplication.class, args);
    }

    @Resource
    ActivityOrderTaskDao activityOrderTaskDao;

    @PostConstruct
    public void preCheck() {
        activityOrderTaskDao.selectAll();
    }
}
