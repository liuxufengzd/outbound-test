package com.rakuten.ecld.wms.wombatoutbound.temp;


import com.rakuten.ecld.wms.wombatoutbound.temp.core.Model;

public interface CommandHandler {
    String getCommand();
    String[] getAlias();
    Model getModel();
    void define();
}
