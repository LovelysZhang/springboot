package com.dubbo.simulate.framework;

import lombok.Getter;

/**
 * 节点的通信连接信息
 * @Date: 2022/10/25
 */
@Getter
public class ClusterURL {
    private String hostname;
    private Integer port;

    public ClusterURL(String hostname, Integer port) {
        this.hostname = hostname;
        this.port = port;
    }
}
