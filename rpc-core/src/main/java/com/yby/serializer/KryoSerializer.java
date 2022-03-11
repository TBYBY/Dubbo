package com.yby.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.yby.RpcException.SerializeException;
import com.yby.entity.RpcRequest;
import com.yby.entity.RpcResponse;
import com.yby.enumeration.SerializerCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class KryoSerializer implements CommonSerializer {

    private static final Logger logger = LoggerFactory.getLogger(KryoSerializer.class);

    /*
        kryo 是线程不安全的，推荐放到 ThreadLocal 中
     */
    private static final ThreadLocal<Kryo> kryoThreadLocal = ThreadLocal.withInitial(() ->{
        Kryo kryo = new Kryo();
        // 注册类，在注册时，回味该序列化类生成 id, 后续在序列化是使用 id 唯一标识该类型
        kryo.register(RpcResponse.class);
        kryo.register(RpcRequest.class);
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        return kryo;
    });

    /*
        kryo 一个基于字节的序列化，序列化时会记录属性对象的类型信息
     */
    @Override
    public byte[] serialize(Object obj) {
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Output output = new Output(byteArrayOutputStream)){
            Kryo kryo = kryoThreadLocal.get();
            kryo.writeObject(output, obj);
            kryoThreadLocal.remove();
            return output.toBytes();
        } catch (Exception e) {
            logger.error("序列化时有错误发生：" , e);
            throw new SerializeException("序列化时有错误发生");
        }
    }

    /*
        反序列化
     */
    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        // 反序列化：从输入流中获取字节流
        // 使用 ThreadLocal 获取 kryo 然后删除 Thread
        try(ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
            Input input = new Input(inputStream)){
            Kryo kryo = kryoThreadLocal.get();
            // 从 thread 中读取对象
            Object o = kryo.readObject(input, clazz);
            kryoThreadLocal.remove();
            return o;
        }catch (Exception e){
            logger.error("反序列化时有错误发生：", e);
            throw new SerializeException("反序列化时有错误发生");
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("KRYO").getCode();
    }
}
