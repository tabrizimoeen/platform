package org.platform.repair.controller;

import org.platform.repair.dto.bale.BaleUpdate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bale")
public class BaleWebhookController {

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveUpdate(@RequestBody BaleUpdate update) {
        if (update.getMessage() != null &&
            update.getMessage().getChat() != null &&
            update.getMessage().getText() != null) {

            Long chatId = update.getMessage().getChat().getId();
            String text = update.getMessage().getText();

            if ("/start".equalsIgnoreCase(text.trim())) {
                // اینجا chatId را در دیتابیس ذخیره کن
                System.out.println("کاربر با chatId = " + chatId + " ربات را start کرد");
            }
        }

        return ResponseEntity.ok("ok");
    }
}
