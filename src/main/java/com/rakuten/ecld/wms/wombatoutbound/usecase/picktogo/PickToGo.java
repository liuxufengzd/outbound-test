package com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo;

import com.rakuten.ecld.wms.wombatoutbound.temp.CommandHandler;
import com.rakuten.ecld.wms.wombatoutbound.temp.StepHandlerFactory;
import com.rakuten.ecld.wms.wombatoutbound.temp.core.Model;
import com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.process.BoxLabelQuestion;
import com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.process.DeliveryQuestion;
import com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.process.RegisterQuestion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PickToGo implements CommandHandler {
    private final StepHandlerFactory stepHandlerFactory;
    private Model model;

    @Override
    public String getCommand() {
        return "picktogo";
    }

    @Override
    public String[] getAlias() {
        return new String[]{"ptg"};
    }

    @Override
    public Model getModel() {
        return model;
    }

    @Override
    public void define() {
        Model model = new Model(stepHandlerFactory);
        this.model = model
                .step("delivery-question").run(DeliveryQuestion.class)
                .step("register-question").run(RegisterQuestion.class)
                .step("box-label-question").run(BoxLabelQuestion.class);
    }
}
