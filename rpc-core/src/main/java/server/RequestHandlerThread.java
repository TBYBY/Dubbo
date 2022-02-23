package server;

import entity.RpcRequest;
import entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;


// 处理线程，接受对象
public class RequestHandlerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);

    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry registry;

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry registry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.registry = registry;
    }

    // 通过 Socket 获取 request, 通过 ServiceRegistry 获取 service
    @Override
    public void run() {
        try(ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream()))
        {
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = registry.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        }catch (IOException | ClassNotFoundException e){
            logger.error("调用或发送时有错误发生：", e);
        }
    }
}
