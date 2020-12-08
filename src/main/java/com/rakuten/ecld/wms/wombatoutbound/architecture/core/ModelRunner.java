package com.rakuten.ecld.wms.wombatoutbound.architecture.core;

import com.rakuten.ecld.wms.wombatoutbound.architecture.RequestObject;

public class ModelRunner {
    private final String latestStepName;
    private final RequestObject request;
    private final Object state;

    public ModelRunner(RequestObject request, String latestStepName, Object state) {
        this.state = state;
        this.request = request;
        this.latestStepName = latestStepName;
    }

    public RequestObject run(Model model) {
        Step step = model.findStep(latestStepName);
        if (step == null)
            return null;
        step.getStepHandler().execute(request,state);
        return request;
    }
}
