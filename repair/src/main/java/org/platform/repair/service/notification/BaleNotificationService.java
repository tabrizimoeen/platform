package org.platform.repair.service.notification;

import org.platform.repair.config.notification.BaleConfig;
import org.platform.repair.dto.NotificationMessage;
import org.platform.repair.enums.NotificationType;
import org.platform.repair.service.abstraction.NotificationChannel;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BaleNotificationService implements NotificationChannel {

    private final RestTemplate restTemplate;
    private final BaleConfig baleConfig;

    public BaleNotificationService(RestTemplate restTemplate, BaleConfig baleConfig) {
        this.restTemplate = restTemplate;
        this.baleConfig = baleConfig;
    }

    @Override
    public NotificationType type() {
        return NotificationType.BALE;
    }

    @Override
    public void send(NotificationMessage message) {
        String token = baleConfig.getToken();

        if (token == null || token.isBlank()) {
            throw new IllegalStateException("توکن بله تنظیم نشده است.");
        }

        String url = "https://tapi.bale.ai/bot" + token + "/sendMessage";

        Map<String, String> body = Map.of(
                "chat_id", message.getTo(),
                "text", message.getContent()
        );

        try {
            String response = restTemplate.postForObject(url, body, String.class);
            System.out.println("Bale response: " + response);
        } catch (Exception e) {
            throw new RuntimeException("خطا در ارسال پیام به بله: " + e.getMessage(), e);
        }
    }
}
