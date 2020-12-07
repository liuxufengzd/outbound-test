package com.rakuten.ecld.wms.wombatoutbound.temp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class AbstractStepHandler implements StepHandler {
    protected MessageSourceUtil messageSourceUtil;

    @Autowired
    public void setMessageSourceUtil(MessageSourceUtil messageSourceUtil) {
        this.messageSourceUtil = messageSourceUtil;
    }
}
