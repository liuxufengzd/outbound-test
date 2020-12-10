package com.rakuten.ecld.wms.wombatoutbound.service;

import com.rakuten.ecld.wms.wombatoutbound.architecture.RequestObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.SenderProperties;
import com.rakuten.ecld.wms.wombatoutbound.architecture.StateUtil;
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
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableConfigurationProperties(SenderProperties.class)
public class Sender {
    private static final NioEventLoopGroup group = new NioEventLoopGroup();
    private static final EventExecutorGroup executorGroup = new DefaultEventExecutorGroup(10);
    private static final Bootstrap bootstrap = new Bootstrap();
    public static long count;
    private Receiver receiver;
    private final Channel[] channels = new Channel[2];
    private static int selectNum;
    private int maxSendNum;
    private String process;
    private SenderProperties senderProperties;
    private boolean counterRunning;

    @Autowired
    public void setSenderProperties(SenderProperties senderProperties) {
        this.senderProperties = senderProperties;
    }
    @Autowired
    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public void send(RequestObject request) {
        if (request != null) {
            sendMessage(makeRequest(request));
        } else if (!counterRunning) {
            counterRunning = true;
            printResult();
        }
    }

    public void initSender(String command) {
        process = command;
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new HttpClientCodec(),
                                new HttpObjectAggregator(1024 * 512 * 1024),
                                new Counter());
                        pipeline.addLast(executorGroup, receiver);
                    }
                });
        try {
            count = senderProperties.getCount();
            channels[0]=bootstrap.connect(senderProperties.getHostName(), senderProperties.getPort()).sync().channel();
            channels[1]=bootstrap.connect(senderProperties.getHostName(), senderProperties.getPort()).sync().channel();
            // send the initial requests to server
            executorGroup.scheduleWithFixedDelay(() -> {
                if (count > 0) {
                    sendMessage(makeRequest((null)));
                    count--;
                }
            }, 0, 1000000 / senderProperties.getRate(), TimeUnit.MICROSECONDS);
            channels[0].closeFuture().sync();
            channels[1].closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private FullHttpRequest makeRequest(RequestObject request) {
        try {
            if (request == null)
                request = new RequestObject(process, null, process, null);
            ByteBuf buf = Unpooled.copiedBuffer(StateUtil.writeValueAsBytes(request));
            DefaultFullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.POST, new URI(senderProperties.getUri()).toASCIIString(), buf);
            // Setting header is important
            httpRequest.headers().set(HttpHeaderNames.HOST, senderProperties.getHostName());
            httpRequest.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, httpRequest.content().readableBytes());
            httpRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            httpRequest.headers().set(HttpHeaderNames.AUTHORIZATION, senderProperties.getToken());
            httpRequest.headers().set(HttpHeaderNames.ACCEPT_LANGUAGE, "ja");
            return httpRequest;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void printResult() {
        executorGroup.scheduleWithFixedDelay(() -> {
            long outNum = Counter.outNum.get();
            long inNum = Counter.inNum.get();
            System.out.println("request sent: " + outNum);
            System.out.println("OK request received: " + inNum);
            String percent = new DecimalFormat("0.000").format((1 - inNum * 1.0 / outNum) * 100);
            System.out.println("failure rate: " + percent + "%");
            System.out.println("========================");
            if (outNum == inNum && count == 0) {
                try {
                    Thread.sleep(1000);
                    channels[0].close().sync();
                    channels[1].close().sync();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                    executorGroup.shutdownGracefully();
                    System.out.println("=======closed=======");
                }
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void sendMessage(FullHttpRequest request) {
        if (maxSendNum < 50) {
            channels[selectNum].writeAndFlush(request);
            maxSendNum++;
        } else {
            executorGroup.execute(()->{
                try {
                    channels[selectNum] = bootstrap.connect(senderProperties.getHostName(), senderProperties.getPort()).sync().channel();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            selectNum = (selectNum + 1) % 2;
            channels[selectNum].writeAndFlush(request);
            maxSendNum = 1;
        }
    }
}
