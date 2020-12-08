package com.rakuten.ecld.wms.wombatoutbound.service;

import com.rakuten.ecld.wms.wombatoutbound.architecture.CommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.ModelState;
import com.rakuten.ecld.wms.wombatoutbound.architecture.RequestObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.ResponseObject;
import com.rakuten.ecld.wms.wombatoutbound.architecture.core.ModelRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MessageHandler implements ApplicationRunner {
    private List<CommandHandler> commandHandlers;
    private Sender sender;

    @Autowired
    public void setCommandHandlers(List<CommandHandler> commandHandlers) {
        this.commandHandlers = commandHandlers;
    }
    @Autowired
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @Override
    public void run(ApplicationArguments args) {
        commandHandlers.forEach(CommandHandler::define);
    }

    public void makeSend(ResponseObject response) {
        sender.send(makeRequest(response));
    }

    private RequestObject makeRequest(ResponseObject response) {
        RequestObject request = new RequestObject();
        request.setProcess(response.getProcess());
        request.setStep(response.getStep());
        request.setStateData(response.getStateData());
        ModelState modelState = findModelForResponse(response);
        if (modelState == null)
            throw new RuntimeException("cannot found the model for command:" + response.getProcess());

        return new ModelRunner(request, request.getStep(), modelState.getState()).run(modelState.getModel());
    }

    private ModelState findModelForResponse(ResponseObject response) {
        String command = response.getProcess();
        for (CommandHandler commandHandler : commandHandlers) {
            String[] alias = commandHandler.getAlias();

            if (command.equals(commandHandler.getCommand()) ||
                    (alias != null && Arrays.asList(alias).contains(command)))
                return new ModelState(commandHandler.getModel(), commandHandler.generateState());
        }
        return null;
    }
}
