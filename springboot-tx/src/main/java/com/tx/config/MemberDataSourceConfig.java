package com.tx.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * @author lovely
 * on 2021/9/23
 */
@Configuration
@MapperScan(sqlSessionFactoryRef = "memberSqlSessionFactory", basePackages = {"com.tx.repository.dao"})
public class MemberDataSourceConfig {


    /**
     * 定义数据源配置
     * @return
     */
    @Bean(name = "memberBaseConf")
    @ConfigurationProperties(prefix = "spring.datasource.member")
    @Primary
    public HikariBaseConf hikaricpBaseConf() {
        return new HikariBaseConf();
    }

    /**
     * 定义数据源
     * @param hikariBaseConf
     * @return
     */
    @Bean("memberDataSource")
    // @ConfigurationProperties(prefix = "spring.datasource.member")
    public DataSource memberDataSource(@Qualifier("memberBaseConf") HikariBaseConf hikariBaseConf) {
        return new HikariDataSource(hikariBaseConf.getHikariConf());
    }

    @Bean(name = "memberSqlSessionFactory")
    public SqlSessionFactory memberSqlSessionFactory(@Qualifier("memberDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    /**
     * 1.定义事务管理器，给其指定一个数据源（可以把事务管理器想象为一个人，这个人来负责事务的控制操作）
     * @param dataSource
     * @return
     */
    @Bean(name = "memberTransactionManager")
    public DataSourceTransactionManager memberTransactionManager(@Qualifier("memberDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 创建sqlSession模板
     *
     * @param sqlSessionFactory
     * @return
     */
    @Bean(name = "memberSqlSessionTemplate")
    public SqlSessionTemplate memberSqlSessionTemplate(@Qualifier("memberSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
