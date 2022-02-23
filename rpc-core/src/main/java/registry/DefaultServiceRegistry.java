package registry;

import RpcException.RpcException;
import enumeration.RpcError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServiceRegistry implements ServiceRegistry{

    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    // 使用 Map 存放 Name, Service
    // 使用 Set 存放 已注册的服务名，每个接口只能有一个服务实现
    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized <T> void register(T service) {
        String name = service.getClass().getCanonicalName();
        if(registeredService.contains(name)) return;
        registeredService.add(name);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        // 一个服务可以实现多个接口
        if(interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }
        for(Class<?> c : interfaces){
            // getCanonicalName() 返回规范名称
            serviceMap.put(c.getCanonicalName(), service);
        }
        logger.info("向接口：{} 注册服务：{}" , interfaces, name);
    }

    @Override
    public synchronized Object getService(String name) {
        Object service = serviceMap.get(name);
        if(service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
