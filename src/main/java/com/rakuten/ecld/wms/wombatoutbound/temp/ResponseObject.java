package com.rakuten.ecld.wms.wombatoutbound.temp;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject {

    private String process;
    private String step;
    private boolean nextStep;
    private JsonNode stateData;
}
