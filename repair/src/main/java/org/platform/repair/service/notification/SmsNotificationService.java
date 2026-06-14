package org.platform.repair.service.notification;

import org.platform.repair.dto.NotificationMessage;
import org.platform.repair.enums.NotificationType;
import org.platform.repair.service.abstraction.NotificationChannel;
import org.platform.repair.service.abstraction.SmsClient;
import org.springframework.stereotype.Service;

@Service
public class SmsNotificationService implements NotificationChannel {

    private final SmsClient smsClient;

    public SmsNotificationService(SmsClient smsClient) {
        this.smsClient = smsClient;
    }

    @Override
    public NotificationType type() {
        return NotificationType.SMS;
    }

    @Override
    public void send(NotificationMessage message) {
        smsClient.sendSms(message.getTo(), message.getContent());
    }
}
