package org.platform.repair.service.abstraction;

public interface SmsClient {
    void sendSms(String to, String message);
}