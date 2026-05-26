package org.platform.repair.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoice", schema = "repairs")
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "repair_order_id", nullable = false)
    private RepairOrder repairOrder;

    private BigDecimal amount;

    @Builder.Default
    private Boolean paid = false;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}