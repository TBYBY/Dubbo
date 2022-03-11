package com.yby.transport;

import com.yby.entity.RpcRequest;
import com.yby.serializer.CommonSerializer;

public interface RpcClient {
    Object sendRequest(RpcRequest request);

    void setSerializer(CommonSerializer serializer);
}
