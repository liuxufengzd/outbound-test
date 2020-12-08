package com.rakuten.ecld.wms.wombatoutbound.architecture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class StepHandlerFactory {
    private ApplicationContext context;
    @Autowired
    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public StepHandler createStepHandler(Class<? extends StepHandler> commandHandlerType) {
        return this.context.getBean(commandHandlerType);
    }
}
