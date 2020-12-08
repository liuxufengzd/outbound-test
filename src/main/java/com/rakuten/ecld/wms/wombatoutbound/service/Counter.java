package com.rakuten.ecld.wms.wombatoutbound.service;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

public class Counter extends ChannelDuplexHandler {
    public static AtomicLong outNum = new AtomicLong(0);
    public static AtomicLong inNum = new AtomicLong(0);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        FullHttpResponse httpResponse = (FullHttpResponse) msg;
        if (httpResponse.status().equals(HttpResponseStatus.OK))
            inNum.incrementAndGet();
        ctx.fireChannelRead(msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        outNum.incrementAndGet();
        ctx.write(msg, promise);
    }
}
