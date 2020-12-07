package com.rakuten.ecld.wms.wombatoutbound.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakuten.ecld.wms.wombatoutbound.temp.RequestObject;
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
    @Value("${test.hostname}")
    private String hostName;
    @Value("${test.port}")
    private String port;
    @Value("${test.uri}")
    private String path;
    @Value("${test.token}")
    private String token;
    private Receiver receiver;

    @Autowired
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void send(RequestObject request) {
        if (channel == null)
            initSender();
        channel.writeAndFlush(makeRequest(request));
    }

    private void initSender() {
        Bootstrap bootstrap = new Bootstrap();
        receiver.setSender(this);
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private FullHttpRequest makeRequest(RequestObject request) {
        try {
            ByteBuf buf = Unpooled.copiedBuffer(new ObjectMapper().writeValueAsBytes(request));
            DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, new URI(path).toASCIIString(), buf);
            // Setting header is important
            httpRequest.headers().set(HttpHeaderNames.HOST, hostName);
            httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpRequest.content().readableBytes());
            httpRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            httpRequest.headers().set(HttpHeaderNames.AUTHORIZATION, token);
            httpRequest.headers().set(HttpHeaderNames.ACCEPT_LANGUAGE, "ja");
            return httpRequest;
        } catch (URISyntaxException | JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
