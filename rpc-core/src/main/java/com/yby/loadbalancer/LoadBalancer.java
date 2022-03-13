package com.yby.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 获取提供这个服务的 instance 的列表
 */
public interface LoadBalancer {
    Instance select(List<Instance> instances);
}
