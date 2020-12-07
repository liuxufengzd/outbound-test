package com.rakuten.ecld.wms.wombatoutbound.usecase.picktogo.model;

import com.rakuten.ecld.wms.wombatoutbound.temp.BaseState;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PtgState extends BaseState {
    private boolean batchRegistered;
    private boolean pickFinished;
    private boolean nextItemFound = true;
    private String batch;
    private int pickNumber;
    private int pickedNumber;
    private int numberExcludeBadItem;
    private Item item = new Item();
}
