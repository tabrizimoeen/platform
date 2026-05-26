package org.platform.repair.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "repair_log",schema = "repairs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long repairId;

    private String message;

    private String status;

    private LocalDateTime createdAt = LocalDateTime.now();
}