package com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Batch {
    private String code;
    private int rFlag;
    private int cFlag;
}
