package com.rakuten.ecld.wms.wombatoutbound.architecture;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestObject {

    private String process;
    private String step;
    private String input;
    private JsonNode stateData;

    public void input(String data){
        input = data;
    }
}
