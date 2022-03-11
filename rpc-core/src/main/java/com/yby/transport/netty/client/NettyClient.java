package com.yby.transport.netty.client;

import com.yby.transport.RpcClient;
import com.yby.RpcException.RpcException;
import com.yby.codec.CommonDecoder;
import com.yby.codec.CommonEncoder;
import com.yby.entity.RpcRequest;
import com.yby.entity.RpcResponse;
import com.yby.enumeration.RpcError;
import com.yby.registry.ServiceRegistry;
import com.yby.registry.NacosServiceRegistry;
import com.yby.serializer.CommonSerializer;
import com.yby.serializer.KryoSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicReference;

public class NettyClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);


    private final ServiceRegistry serviceRegistry;
    private static final Bootstrap bootstrap;
    private CommonSerializer serializer;

    public NettyClient() {
        serviceRegistry = new NacosServiceRegistry();
    }

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true);
    }

    @Override
    public Object sendRequest(RpcRequest request) {
        if(serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        AtomicReference<Object> result = new AtomicReference<>(null);
        try{
            InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(request.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if(channel != null){
                channel.writeAndFlush(request).addListener(future1 -> {
                    if(future1.isSuccess()){
                        logger.info(String.format("客户端发送消息： %s", request.toString()));
                    }else {
                        logger.info("发送消息时有错误发生：", future1.cause());
                    }
                });
                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse" + request.getRequestId());
                RpcResponse rpcResponse = channel.attr(key).get();
                return rpcResponse.getData();
            }
        }catch (InterruptedException e){
            logger.error("发送消息时有错误发生：", e);
        }
        return null;
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}
