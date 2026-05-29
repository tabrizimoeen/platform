package org.platform.repair.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoice", schema = "repairs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "repair_order_id", unique = true)
    private RepairOrder repairOrder;

    private BigDecimal amount;

    private Boolean paid;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private RepairShop shop;
    private LocalDateTime createdAt;
}