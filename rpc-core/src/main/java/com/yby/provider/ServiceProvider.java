package com.yby.provider;

// 保存本地信息
public interface ServiceProvider {
    // 加入 service
    <T> void addServiceProvider(T service);
    // 获取 service
    Object getServiceProvider(String serviceName);
}
