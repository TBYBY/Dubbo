package com.yby.socket.client;

import com.yby.entity.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);

    public Object sendRequest(RpcRequest request, String host, int port){
        try (Socket socket = new Socket(host, port)){
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(request);
            objectOutputStream.flush();
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时产生错误" , e);
            return null;
        }
    }
}
