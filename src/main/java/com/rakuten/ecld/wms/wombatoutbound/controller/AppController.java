package com.rakuten.ecld.wms.wombatoutbound.controller;

import com.rakuten.ecld.wms.wombatoutbound.service.Sender;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AppController {
    private final Sender sender;

    @GetMapping("/test/{command}")
    public void startTest(@PathVariable("command") String command){
        sender.initSender(command);
    }
}
