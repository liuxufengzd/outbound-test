package com.rakuten.ecld.wms.wombatoutbound.service;

import com.rakuten.ecld.wms.wombatoutbound.architecture.*;
import com.rakuten.ecld.wms.wombatoutbound.architecture.core.ModelRunner;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.CharsetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@ChannelHandler.Sharable
public class Receiver extends SimpleChannelInboundHandler<FullHttpResponse> implements ApplicationRunner {
    private Sender sender;
    private List<CommandHandler> commandHandlers;
    @Autowired
    public void setCommandHandlers(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpResponse msg) throws Exception {
        String content = msg.content().toString(CharsetUtil.UTF_8);
        ResponseObject responseObject = StateUtil.readValue(content, ResponseObject.class);
        RequestObject requestObject = makeRequest(responseObject);
        sender.send(requestObject);
    }

    private RequestObject makeRequest(ResponseObject response){
        RequestObject request = new RequestObject();
        request.setProcess(response.getProcess());
        request.setStep(response.getStep());
        request.setStateData(response.getStateData());
        ModelState modelState = findModelForResponse(response);
        if (modelState == null)
            throw new RuntimeException("cannot found the model for command:"+response.getProcess());

        return new ModelRunner(request,request.getStep(),modelState.getState()).run(modelState.getModel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private ModelState findModelForResponse(ResponseObject response){
        String command = response.getProcess();
        for (CommandHandler commandHandler:commandHandlers){
            String[] alias = commandHandler.getAlias();

            if (command.equals(commandHandler.getCommand()) ||
                    (alias!=null && Arrays.asList(alias).contains(command)))
                return new ModelState(commandHandler.getModel(),commandHandler.generateState());
        }
        return null;
    }

    @Override
    public void run(ApplicationArguments args) {
        commandHandlers.forEach(CommandHandler::define);
    }
}