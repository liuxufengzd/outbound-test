package com.rakuten.ecld.wms.wombatoutbound.temp;

@FunctionalInterface
public interface StepHandler {
    void execute(RequestObject request);
}
