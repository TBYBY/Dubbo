package com.yby.registry;

// 保存本地信息
public interface ServiceRegistry {
    // 加入 service
    <T> void register(T service);
    // 获取 service
    Object getService(String name);
}
