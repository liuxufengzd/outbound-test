package com.rakuten.ecld.wms.wombatoutbound.temp;

import com.rakuten.ecld.wms.wombatoutbound.temp.core.Model;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ModelState {
    private Model model;
    private Object state;
}
