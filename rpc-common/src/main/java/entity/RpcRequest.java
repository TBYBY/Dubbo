package entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder

/*
客户端传输需要调用的 interface 和 方法 以及对应参数（防止重载）
确定传输格式
1. 接口名
2. 方法名
3. 参数类型
4. 参数实际值
 */
public class RpcRequest implements Serializable {
    /*
    待调用接口名
     */
    private String interfaceName;

    /*
    待调用方法名
     */
    private String methodName;

    /*
    调用方法的参数
     */
    private Object[] parameters;

    /*
    调用方法的参数类型
     */
    private Class<?>[] paramTypes;
}
