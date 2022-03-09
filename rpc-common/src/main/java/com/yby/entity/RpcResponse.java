package com.yby.entity;

import com.yby.enumeration.ResponseCode;
import lombok.Data;

import java.io.Serializable;

@Data
/*
返回调用的信息，调用成功还是失败
 */
public class RpcResponse<T> implements Serializable {
    /*
    响应状态码, 可进行自定义
     */
    private Integer statusCode;

    /*
    响应状态补充信息
     */
    private String message;

    /*
    响应数据
     */
    private T data;

    public static <T> RpcResponse<T> success(T data){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
