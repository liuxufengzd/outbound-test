package com.rakuten.ecld.wms.wombatoutbound.architecture;

@FunctionalInterface
public interface StepHandler<T> {
    void execute(RequestObject request, T state);
}
