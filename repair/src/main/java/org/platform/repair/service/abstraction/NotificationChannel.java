package org.platform.repair.service.abstraction;

import org.platform.repair.dto.NotificationMessage;
import org.platform.repair.enums.NotificationType;

public interface NotificationChannel {
    NotificationType type();
    void send(NotificationMessage message);
}
