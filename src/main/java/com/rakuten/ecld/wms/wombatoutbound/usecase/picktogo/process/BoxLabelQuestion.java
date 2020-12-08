package com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.process;

import com.rakuten.ecld.wms.wombatoutbound.architecture.AbstractStepHandler;
import com.rakuten.ecld.wms.wombatoutbound.architecture.RequestObject;
import com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.model.PtgState;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class BoxLabelQuestion extends AbstractStepHandler<PtgState> {

    @Override
    public void process(PtgState requestState, RequestObject request) {
        request.input("BOX" + new Random().nextInt(100));
    }
}
