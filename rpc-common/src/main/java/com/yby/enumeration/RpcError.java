package com.yby.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RpcError {

    SERVICE_INVOCATION_FAILURE("服务调用出现失败"),
    SERVICE_NOT_FOUND("找不到对应服务"),
    SERVICE_NOT_IMPLEMENT_ANY_INTERFACE("注册的服务未实现任何接口"),
    UNKNOWN_PROTOCOL("不能被识别的协议包"),
    UNKNOWN_PACKAGE_TYPE("不能识别的数据包"),
    UNKNOWN_SERIALIZER("不能识别的反序列化器"),
    FAILED_TO_CONNECT_TO_SERVICE_REGISTRY("不能连接到Nacos"),
    REGISTER_SERVICE_FAILED("注册服务时有错误发生"),
    SERIALIZER_NOT_FOUND("未设置序列化器"),
    RESPONSE_NOT_MATCH("响应与请求号不匹配"),
    CLIENT_CONNECT_SERVER_FAILURE("客户端连接失败");

    private final String message;
}
