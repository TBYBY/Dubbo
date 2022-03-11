package com.yby.transport;

import com.yby.serializer.CommonSerializer;

public interface RpcServer {
    void start();

    <T> void publishService(Object service, Class<T> serviceClass);

    void setSerializer(CommonSerializer serializer);
}
