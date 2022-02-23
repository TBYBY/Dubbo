package client;

import entity.RpcRequest;
import entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 没有客户端的实例对象，使用动态代理生成实例
 * 并将 rpcRequest 发送给服务端
 * 代理类
 */
public class RpcClientProxy implements InvocationHandler {
    /*
    主机地址
     */
    private String host;
    /*
    对应端口
     */
    private int port;

    public RpcClientProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    // 忽略 unchecked 错误
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz){
        // 传入 接口的 classLoader, 传入
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 代理对象被代理时的动作
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .parameters(args)
                .paramTypes(method.getParameterTypes())
                .build();
        RpcClient rpcClient = new RpcClient();
        return ((RpcResponse) rpcClient.sendRequest(rpcRequest,host,port)).getData();
    }


}
