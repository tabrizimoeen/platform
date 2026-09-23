package org.platform.repair.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "repair_logs", schema = "repairs")
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
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private RepairShop shop;
    private LocalDateTime createdAt = LocalDateTime.now();
}