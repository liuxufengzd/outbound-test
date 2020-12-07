package com.rakuten.ecld.wms.wombatoutbound.temp;

@FunctionalInterface
public interface StepHandler<T> {
    void execute(RequestObject request, T state);
}
