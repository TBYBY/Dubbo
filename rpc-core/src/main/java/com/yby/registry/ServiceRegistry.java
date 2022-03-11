package com.yby.registry;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public interface ServiceRegistry {
    // 注册服务
    void register(String serviceName, InetSocketAddress inetSocketAddress);

    // 查询服务
    InetSocketAddress lookupService(String serviceName);
}
