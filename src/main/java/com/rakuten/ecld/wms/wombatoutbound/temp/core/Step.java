package com.rakuten.ecld.wms.wombatoutbound.temp.core;

import com.rakuten.ecld.wms.wombatoutbound.temp.StepHandler;

public class Step {
    private final String stepName;
    private final Model model;
    private StepHandler stepHandler;

    public Step(String stepName, Model model) {
        this.stepName = stepName;
        this.model = model;
    }

    StepHandler getStepHandler() {
        return stepHandler;
    }

    String getName() {
        return stepName;
    }

    public Model run(Class<? extends StepHandler> handlerType) {
        stepHandler = model.getStepHandlerFactory().createStepHandler(handlerType);
        return model;
    }
}
