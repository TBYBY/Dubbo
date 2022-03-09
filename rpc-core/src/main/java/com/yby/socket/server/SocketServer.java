package com.yby.socket.server;

import com.yby.RequestHandler;
import com.yby.RpcServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.yby.registry.ServiceRegistry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/*
使用反射调用
使用一个ServerSocket 监听某个端口
循环接受连接请求并在新线程中处理
 */
public class SocketServer implements RpcServer {
    /*
    建立线程池
     */
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    private static final int corePoolSize = 5;
    private static final int maximumPoolSize = 50;
    private static final int keepAliveTime = 60;
    private final ExecutorService threadPool;
    private RequestHandler requestHandler = new RequestHandler();
    private final ServiceRegistry registry;

    public SocketServer(ServiceRegistry serviceRegistry) {
        this.registry = serviceRegistry;
        // 阻塞队列
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    /*
    添加 register 提供接口调用方法
     */
    public void start(int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("消费者连接：{}：{} ", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket, requestHandler, registry));
            }
            threadPool.shutdown();
        }catch (IOException e){
            logger.error("连接时有错误发生：", e);
        }
    }

}
