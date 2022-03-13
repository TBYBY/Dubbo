package com.yby.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * 随机算法
 */
public class RandomLoadBalancer implements LoadBalancer{

    /**
     * 从提供服务的列表中随机找一个
     * @param instances
     * @return
     */
    @Override
    public Instance select(List<Instance> instances) {
        return instances.get(new Random().nextInt(instances.size()));
    }
}
