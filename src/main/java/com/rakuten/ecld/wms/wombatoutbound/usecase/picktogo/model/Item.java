package com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    private String deliveryCode;
    private String itemCode;
    private String name;
    private String shelfCode;
    private String number;
    private String source;
    private String boxLabel;
    private String boxArea;
}
