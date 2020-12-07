package com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.temp.AbstractStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.temp.RequestObject;
import com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.model.PtgState;
import org.springframework.stereotype.Component;

@Component
public class RegisterQuestion extends AbstractStepHandler<PtgState> {

    @Override
    public void process(PtgState requestState, RequestObject request) {
        request.input(requestState.getItem().getDeliveryCode());
    }
}
