import com.yby.Services.HelloObject;
import com.yby.Services.HelloService;
import com.yby.Netty.client.NettyClient;
import com.yby.RpcClient;
import com.yby.RpcClientProxy;

public class TestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}
