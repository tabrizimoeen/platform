package org.platform.repair.service;

import org.platform.repair.config.notification.BaleConfig; // فرض می‌کنیم این کلاس را قبلا داشتیم
import org.platform.repair.dto.bale.BaleUpdate;
import org.platform.repair.dto.NotificationMessage; // از کلاس قبلی
import org.platform.repair.enums.NotificationType;
import org.platform.repair.service.notification.NotificationDispatcherService; // از کلاس قبلی
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service
public class BaleWebhookService {

    private final BaleConfig baleConfig;
    private final NotificationDispatcherService notificationDispatcherService;
    private final RestTemplate restTemplate; // برای ارسال پیام خوش‌آمد

    public BaleWebhookService(BaleConfig baleConfig, NotificationDispatcherService notificationDispatcherService, RestTemplate restTemplate) {
        this.baleConfig = baleConfig;
        this.notificationDispatcherService = notificationDispatcherService;
        this.restTemplate = restTemplate;
    }

    /**
     * پردازش آپدیت دریافتی از بله.
     * @param update آبجکت BaleUpdate که از بله دریافت شده.
     */
    public void processUpdate(BaleUpdate update) {
        // فقط پیام‌های معمولی (نه کانال، گروه و...) را پردازش کن
        if (update.getMessage() != null && update.getMessage().getChat() != null && "private".equals(update.getMessage().getChat().getType())) {
            Long chatId = update.getMessage().getChat().getId();
            String text = update.getMessage().getText();

            // اگر کاربر دستور /start را فرستاده بود
            if (text != null && text.trim().equalsIgnoreCase("/start")) {
                System.out.println("کاربر با Chat ID: " + chatId + " ربات را Start کرد.");

                // --- مرحله مهم: ذخیره chat_id در دیتابیس ---
                // در اینجا باید chatId را به کاربر مربوطه در سیستم خودت لینک کنی.
                // فرض می‌کنیم شما یک سرویس یا ریپازیتوری برای مدیریت کاربران داری.
                // مثال:
                // userRepository.updateUserBaleChatId(userIdFromToken, chatId);
                // یا اگر کاربر ناشناس است:
                // User user = new User();
                // user.setBaleChatId(chatId);
                // user.setFirstName(update.getMessage().getChat().getFirstName());
                // userRepository.save(user);
                // --- پایان مرحله ذخیره ---

                // ارسال پیام خوش‌آمد به کاربر
                sendWelcomeMessage(chatId);

            } else {
                // اگر پیام دیگری بود (مثلاً کاربر سوالی پرسید)
                System.out.println("پیام از Chat ID " + chatId + ": " + text);
                // اینجا می‌توانید منطق پاسخ به پیام‌های دیگر را پیاده‌سازی کنید.
            }
        }
    }

    /**
     * ارسال پیام خوش‌آمد به کاربر از طریق بله.
     * @param chatId شناسه چت کاربر.
     */
    private void sendWelcomeMessage(Long chatId) {
        String token = baleConfig.getToken();
        if (token == null || token.isBlank()) {
            System.err.println("توکن ربات بله تنظیم نشده است. پیام خوش‌آمد ارسال نشد.");
            return;
        }

        // ساخت پیام
        String welcomeText = "سلام! به ربات اطلاع‌رسانی ما خوش آمدید.\n" +
                             "شما با موفقیت ربات را فعال کردید.";
        NotificationMessage message = new NotificationMessage(String.valueOf(chatId), "" ,welcomeText,new HashMap<>());

        // استفاده از NotificationDispatcherService برای ارسال
        try {
            notificationDispatcherService.send(NotificationType.BALE, message);
            System.out.println("پیام خوش‌آمد با موفقیت به Chat ID " + chatId + " ارسال شد.");
        } catch (Exception e) {
            System.err.println("خطا در ارسال پیام خوش‌آمد به Chat ID " + chatId + ": " + e.getMessage());
            // اینجا می‌توانید لاگ خطا را دقیق‌تر کنید یا مکانیزم retry اضافه کنید.
        }
    }
}
