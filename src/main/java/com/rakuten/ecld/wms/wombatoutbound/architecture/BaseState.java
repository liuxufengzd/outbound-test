package com.rakuten.ecld.wms.wombatoutbound.architecture;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class BaseState {
    private final Map<String,String> flowToRootStep = new HashMap<>();
    private final Map<String,String> flowToCallerStep = new HashMap<>();
}
