package com.rakuten.ecld.wms.wombatoutbound.temp.core;

import com.rakuten.ecld.wms.wombatoutbound.temp.RequestObject;

public class ModelRunner {
    private final String latestStepName;
    private final RequestObject request;

    public ModelRunner(RequestObject request, String latestStepName) {
        this.request = request;
        this.latestStepName = latestStepName;
    }

    public RequestObject run(Model model) {
        model.findStep(latestStepName).getStepHandler().execute(request);
        return request;
    }
}
