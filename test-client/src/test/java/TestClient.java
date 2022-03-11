import com.yby.Services.HelloObject;
import com.yby.Services.HelloService;
import com.yby.transport.netty.client.NettyClient;
import com.yby.transport.RpcClient;
import com.yby.transport.RpcClientProxy;
import com.yby.serializer.ProtobufSerializer;

public class TestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        client.setSerializer(new ProtobufSerializer());
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
