package org.platform.repair.service;

import org.platform.repair.config.notification.BaleConfig;
import org.platform.repair.service.abstraction.NotificationService;
import org.springframework.beans.factory.annotation.Autowired; // یا از constructor injection استفاده کنید
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate; // یا WebClient

import java.util.Map;

@Service
public class BaleNotificationService implements NotificationService {

    // استفاده از RestTemplate (یا WebClient) برای فراخوانی API
    private final RestTemplate restTemplate;
    private final BaleConfig baleConfig; // تزریق کلاس تنظیمات

    // Constructor Injection: روش پیشنهادی در Spring
    @Autowired
    public BaleNotificationService(RestTemplate restTemplate, BaleConfig baleConfig) {
        this.restTemplate = restTemplate;
        this.baleConfig = baleConfig;
    }

    @Override
    public void send(String chatId, String message) {
        String botToken = baleConfig.getToken(); // خواندن توکن از تنظیمات
        if (botToken == null || botToken.isEmpty()) {
            throw new IllegalStateException("Bale bot token is not configured.");
        }

        String url = "https://tapi.bale.ai/bot" + botToken + "/sendMessage";
        
        // ساخت بدنه پیام به فرمت JSON
        Map<String, String> request = Map.of(
                "chat_id", chatId,
                "text", message
        );

        try {
            // ارسال درخواست HTTP POST
            // RestTemplate برای سادگی، اما WebClient برای async بهتر است
            String response = restTemplate.postForObject(url, request, String.class);
            System.out.println("Bale API Response: " + response); // لاگ کردن پاسخ API
        } catch (Exception e) {
            System.err.println("Error sending message to Bale: " + e.getMessage());
            // در یک سیستم واقعی، بهتر است خطا را لاگ کرده و شاید یک استثنای خاص پرتاب کنید
        }
    }
}
