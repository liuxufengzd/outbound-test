package com.rakuten.ecld.wms.wombatoutbound.architecture.core;

import com.rakuten.ecld.wms.wombatoutbound.architecture.StepHandlerFactory;

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
        return filteredSteps.size() == 0 ? null : filteredSteps.get(0);
    }
}
