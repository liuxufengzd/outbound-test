package com.rakuten.ecld.wms.wombatoutbound.temp;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
