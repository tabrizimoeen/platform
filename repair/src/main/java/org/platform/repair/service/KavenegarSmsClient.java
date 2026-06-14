package org.platform.repair.service;

import org.platform.repair.service.abstraction.SmsClient;
import org.springframework.stereotype.Component;

@Component
public class KavenegarSmsClient implements SmsClient {

    @Override
    public void sendSms(String to, String message) {
        // اینجا باید API پنل پیامکی واقعی را صدا بزنی
        System.out.println("ارسال پیامک به " + to + " با متن: " + message);
    }
}
