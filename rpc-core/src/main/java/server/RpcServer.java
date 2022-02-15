package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/*
使用反射调用
使用一个ServerSocket 监听某个端口
循环接受连接请求并在新线程中处理
 */
public class RpcServer {
    /*
    建立线程池
     */
    private final ExecutorService threadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    public RpcServer() {
        int corePoolSize = 5;
        int maximumPoolSize = 50;
        int keepAliveTime = 60;
        // 阻塞队列
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    /*
    添加 register 提供接口调用方法
     */
    public void register(Object service, int port){
        try(ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器正在启动...");
            Socket socket;
            while((socket = serverSocket.accept()) != null){
                logger.info("客户端连接！IP为：" + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket, service));
            }
        }catch (IOException e){
            logger.error("连接时有错误发生：", e);
        }
    }

}
