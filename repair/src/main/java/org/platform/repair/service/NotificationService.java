package org.platform.repair.service;

import org.platform.repair.dto.NotificationMessage;
import org.platform.repair.enums.NotificationType;
import org.platform.repair.service.abstraction.NotificationChannel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final Map<NotificationType, NotificationChannel> channels;

    public NotificationService(List<NotificationChannel> channelList) {
        this.channels = channelList.stream()
                .collect(Collectors.toMap(
                        NotificationChannel::type,
                        c -> c
                ));
    }

    public void send(NotificationType type, NotificationMessage message) {
        channels.get(type).send(message);
    }
}