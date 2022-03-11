package com.yby.provider;

import com.yby.RpcException.RpcException;
import com.yby.enumeration.RpcError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// 这样保证对于不同的线程，一次只能有一个线程进行调用，防止出错
public class ServiceProviderImpl implements ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    // 使用 Map 存放 Name, Service
    // 使用 Set 存放 已注册的服务名，每个接口只能有一个服务实现
    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public <T> void addServiceProvider(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if(registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        // 一个服务可以实现多个接口
        if(interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> c : interfaces){
            // getCanonicalName() 返回规范名称
            serviceMap.put(c.getCanonicalName(), service);
        }
        logger.info("向接口：{} 注册服务：{}" , interfaces, serviceName);
    }

    @Override
    public Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
