package com.tx.config;

import com.zaxxer.hikari.HikariConfig;
import lombok.Data;

/**
 * @author lovely
 * on 2021/9/24
 */
@Data
public class HikariBaseConf {
    // private String url;
    private String namespace;
    private String type;
    private String username;
    private String password;
    private String dbname;
    private int corePoolSize;
    private int maxPoolSize;
    private String jdbcUrlOption;
    private String commit;

    public HikariConfig getHikariConf() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/lovelys?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false");
        config.setUsername(this.username);
        config.setPassword(this.password);
        config.setMaximumPoolSize(20);
        config.addDataSourceProperty("cachePrepStmts", "true"); //是否自定义配置，为true时下面两个参数才生效
        config.addDataSourceProperty("prepStmtCacheSize", "250"); //连接池大小默认25，官方推荐250-500
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); //单条语句最大长度默认256，官方推荐2048
        config.addDataSourceProperty("useServerPrepStmts", "true"); //新版本MySQL支持服务器端准备，开启能够得到显著性能提升
        config.addDataSourceProperty("useLocalSessionState", "true");
        // true 事务会失效
        // config.addDataSourceProperty("useLocalTransactionState", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("cacheResultSetMetadata", "true");
        config.addDataSourceProperty("cacheServerConfiguration", "true");
        config.addDataSourceProperty("elideSetAutoCommits", "true");
        config.addDataSourceProperty("maintainTimeStats", "false");
        config.setAutoCommit(false);
        return config;
    }
}
