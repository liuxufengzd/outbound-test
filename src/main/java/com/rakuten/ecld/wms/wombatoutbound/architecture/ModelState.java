package com.rakuten.ecld.wms.wombatoutbound.architecture;

import com.rakuten.ecld.wms.wombatoutbound.architecture.core.Model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModelState {
    private Model model;
    private Object state;
}
