package com.rakuten.ecld.wms.wombatoutbound.service;

import com.rakuten.ecld.wms.wombatoutbound.temp.RequestObject;
import com.rakuten.ecld.wms.wombatoutbound.temp.StateUtil;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;

@Component
public class Sender {
    private static final NioEventLoopGroup group = new NioEventLoopGroup();
    private static final EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(10);
    private Channel channel;
    private String process;
    private Receiver receiver;
    @Value("${test.hostname}")
    private String hostName;
    @Value("${test.port}")
    private String port;
    @Value("${test.uri}")
    private String path;
    @Value("${test.token}")
    private String token;
    @Value("${test.count}")
    private String count;

    @Autowired
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void send(RequestObject request) {
        if (request != null) {
            channel.writeAndFlush(makeRequest(request));
        } else shutDown();
    }

    public void initSender(String command) {
        Bootstrap bootstrap = new Bootstrap();
        receiver.setSender(this);
        process = command;
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpClientCodec(),
                                new HttpObjectAggregator(512 * 1024));
                        pipeline.addLast(executorGroup, receiver);
                    }
                });
        try {
            // don't need to close the channel unless you stop the pressure test
            channel = bootstrap.connect(hostName, Integer.parseInt(port)).sync().channel();
            // send the initial requests
            executorGroup.execute(() -> {
                int number = Integer.parseInt(count);
                while (number > 0) {
                    channel.writeAndFlush(makeRequest(null));
                    number--;
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private FullHttpRequest makeRequest(RequestObject request) {
        try {
            if (request == null)
                request = new RequestObject(process, null, process, null);
            ByteBuf buf = Unpooled.copiedBuffer(StateUtil.writeValueAsBytes(request));
            DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, new URI(path).toASCIIString(), buf);
            // Setting header is important
            httpRequest.headers().set(HttpHeaderNames.HOST, hostName);
            httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpRequest.content().readableBytes());
            httpRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            httpRequest.headers().set(HttpHeaderNames.AUTHORIZATION, token);
            httpRequest.headers().set(HttpHeaderNames.ACCEPT_LANGUAGE, "ja");
            return httpRequest;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void shutDown() {
        try {
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
            executorGroup.shutdownGracefully();
        }
    }
}
