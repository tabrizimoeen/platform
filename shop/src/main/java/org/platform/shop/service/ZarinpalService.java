package org.platform.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ZarinpalService {

    @Value("${zarinpal.merchant-id}")
    private String merchantId;

    @Value("${zarinpal.request-url}")
    private String requestUrl;

    @Value("${zarinpal.startpay-url}")
    private String startPayUrl;

    private final RestTemplate restTemplate;

    public String requestPayment(Long amount, Long orderId) {

        Map<String, Object> data = new HashMap<>();

        data.put("merchant_id", merchantId);
        data.put("amount", amount);
        data.put("callback_url",
                "http://localhost:8080/api/payments/callback?orderId=" + orderId);
        data.put("description", "Order payment");

        Map<String, Object> request = new HashMap<>();
        request.put("data", data);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(requestUrl, request, Map.class);

        Map<String, Object> res = (Map<String, Object>) response.getBody().get("data");

        String authority = (String) res.get("authority");

        return startPayUrl + authority;
    }
}