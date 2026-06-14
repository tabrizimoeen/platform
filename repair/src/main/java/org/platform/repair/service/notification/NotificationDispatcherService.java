package org.platform.repair.service.notification;

import org.platform.repair.dto.NotificationMessage;
import org.platform.repair.enums.NotificationType;
import org.platform.repair.service.abstraction.NotificationChannel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationDispatcherService {

    private final Map<NotificationType, NotificationChannel> channels;

    public NotificationDispatcherService(List<NotificationChannel> channelList) {
        this.channels = channelList.stream()
                .collect(Collectors.toMap(
                        NotificationChannel::type,
                        channel -> channel
                ));
    }

    public void send(NotificationType type, NotificationMessage message) {
        NotificationChannel channel = channels.get(type);

        if (channel == null) {
            throw new IllegalArgumentException("کانالی برای این نوع پیام پیدا نشد: " + type);
        }

        channel.send(message);
    }
}
