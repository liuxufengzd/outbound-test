package com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.temp.AbstractStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.temp.RequestObject;
import org.springframework.stereotype.Component;

@Component
public class BoxLabelQuestion extends AbstractStepHandler {

    @Override
    public void execute(RequestObject request) {
        request.input(messageSourceUtil.getMessage("test.ptg.box_code_1"));
    }
}
