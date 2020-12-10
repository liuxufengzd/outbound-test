package com.rakuten.ecld.wms.wombatoutbound.service;

import com.rakuten.ecld.wms.wombatoutbound.architecture.*;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@ChannelHandler.Sharable
@Component
public class Receiver extends SimpleChannelInboundHandler<FullHttpResponse> {
    private MessageHandler messageHandler;
    @Autowired
    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) {
        String content = msg.content().toString(CharsetUtil.UTF_8);
        ResponseObject responseObject = StateUtil.readValue(content, ResponseObject.class);
        messageHandler.makeSend(responseObject);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}