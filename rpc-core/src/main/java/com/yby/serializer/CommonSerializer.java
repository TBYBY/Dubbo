package com.yby.serializer;

import com.esotericsoftware.kryo.KryoSerializable;

public interface CommonSerializer {

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code) {
        switch (code) {
            case 0:
                return new KryoSerializer();
            case 1:
                return new JsonSerializer();
            case 2:
                return new ProtobufSerializer();
            default:
                return null;
        }
    }

}
