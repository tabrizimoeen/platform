package org.platform.repair.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RepairLog {
    private final String message;
    private final LocalDateTime createdAt;
}
