package com.yby;

import com.yby.entity.RpcRequest;

public interface RpcClient {
    Object sendRequest(RpcRequest request);
}
