package org.platform.repair.service;

import org.platform.repair.service.abstraction.SmsClient;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class KaavehNegarSmsClient implements SmsClient {

    private final RestTemplate restTemplate;

    public KaavehNegarSmsClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void sendSms(String to, String message) {
        String url = "https://api.kavenegar.com/v1/YOUR_API_KEY/sms/send.json";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("receptor", to);
        body.add("message", message);

        restTemplate.postForObject(url, body, String.class);
    }
}