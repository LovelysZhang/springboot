package com.tx.example;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @author lovely
 * on 2021/9/28
 */
public class EntryPoint {

    /**
     * 编程式事务
     */
    @Test
    public void programmaticTransaction() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/lovelys?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false");
        config.setUsername("root");
        config.setPassword("123456");
        HikariDataSource dataSource = new HikariDataSource(config);
        //定义一个JdbcTemplate，用来方便执行数据库增删改查
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        //1.定义事务管理器，给其指定一个数据源（可以把事务管理器想象为一个人，这个人来负责事务的控制操作）
        PlatformTransactionManager platformTransactionManager = new DataSourceTransactionManager(dataSource);
        //2.定义事务属性：TransactionDefinition，TransactionDefinition可以用来配置事务的属性信息，比如事务隔离级别、事务超时时间、事务传播方式、是否是只读事务等等。
        TransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        //3.获取事务：调用platformTransactionManager.getTransaction开启事务操作，得到事务状态(TransactionStatus)对象
        TransactionStatus transactionStatus = platformTransactionManager.getTransaction(transactionDefinition);
        //4.执行业务操作，下面就执行2个插入操作
        try {
            System.out.println("************before************:" + jdbcTemplate.queryForList("SELECT * from tb_activity_order_task"));
            jdbcTemplate.update("insert into tb_activity_order_task (`user_name`,`activity_id`,`task_id`,`status`) values (?,?,?,?)", "test1-1","order-task","taskid-aaa",0);
            jdbcTemplate.update("insert into tb_activity_order_task (`user_name`,`activity_id`,`task_id`,`status`) values (?,?,?,?)", "test2-2","order-task","taskid-bbb",0);
            //5.提交事务：platformTransactionManager.commit
            platformTransactionManager.commit(transactionStatus);
        } catch (Exception e) {
            //6.回滚事务：platformTransactionManager.rollback
            platformTransactionManager.rollback(transactionStatus);
        }
        System.out.println("************after************:" + jdbcTemplate.queryForList("SELECT * from tb_activity_order_task"));
    }
}
