package com.rakuten.ecld.wms.wombatoutbound.temp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractStepHandler<T> implements StepHandler<T> {
    protected MessageSourceUtil messageSourceUtil;
    protected InputFile inputFile;

    @Autowired
    public void setMessageSourceUtil(MessageSourceUtil messageSourceUtil) {
        this.messageSourceUtil = messageSourceUtil;
    }

    @Autowired
    public void setInputFile(InputFile inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public void execute(RequestObject request, T state) {
        Object stateObject = StateUtil.deserializeState(request.getStateData(), state);
        process((T) stateObject, request);
        request.setStateData(StateUtil.serialize(stateObject));
    }

    public abstract void process(T requestState, RequestObject request);
}
