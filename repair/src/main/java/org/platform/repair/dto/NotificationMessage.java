package org.platform.repair.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Data
@RequiredArgsConstructor
public class NotificationMessage {

    private final String to;          // شماره یا userId در پیام‌رسان
    private final String title;       // اختیاری (برای پیام‌رسان‌ها مهم‌تره)
    private final String content;

    private final Map<String, Object> metadata; // برای توسعه آینده (templateId, priority, ...)

    // getters & setters
}