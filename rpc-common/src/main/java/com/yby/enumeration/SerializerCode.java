package com.yby.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/**
 * 标识序列化和反序列化器
 */
public enum SerializerCode {
    JSON(1);

    private final int code;
}
