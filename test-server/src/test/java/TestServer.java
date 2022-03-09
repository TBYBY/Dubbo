import com.yby.Services.HelloService;
import com.yby.registry.DefaultServiceRegistry;
import com.yby.registry.ServiceRegistry;
import com.yby.socket.server.SocketServer;

public class TestServer {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        HiServiceImpl hiService = new HiServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        registry.register(hiService);
        SocketServer rpcServer = new SocketServer(registry);
        rpcServer.start(9000);
    }
}
