package com.rakuten.ecld.wms.wombatoutbound.temp.core;

import com.rakuten.ecld.wms.wombatoutbound.temp.StepHandlerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Model {
    private final List<Step> steps = new ArrayList<>();
    private final StepHandlerFactory stepHandlerFactory;

    public Model(StepHandlerFactory stepHandlerFactory) {
        this.stepHandlerFactory = stepHandlerFactory;
    }

    StepHandlerFactory getStepHandlerFactory() {
        return stepHandlerFactory;
    }

    public Step step(String stepName) {
        if (this.steps.stream().anyMatch(s -> s.getName().equals(stepName)))
            throw new RuntimeException("Step name is repeated");
        Step step = new Step(stepName, this);
        steps.add(step);
        return step;
    }

    Step findStep(String stepName) {
        List<Step> filteredSteps = this.steps.stream()
                .filter(step -> step.getName().equals(stepName)).collect(Collectors.toList());
        if (filteredSteps.size() == 0)
            throw new RuntimeException("The step cannot be found :" + stepName);
        return filteredSteps.get(0);
    }
}
