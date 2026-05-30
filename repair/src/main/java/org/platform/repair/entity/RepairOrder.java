package org.platform.repair.entity;

import jakarta.persistence.*;
import lombok.*;
import org.platform.repair.enums.RepairStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "repair_orders", schema = "repairs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne
    @JoinColumn(name = "shop_id")
    private RepairShop shop;

    private String deviceModel;

    @Column(columnDefinition = "TEXT")
    private String problemDescription;

    @Enumerated(EnumType.STRING)
    private RepairStatus status;

    private BigDecimal estimatedCost;

    private BigDecimal finalCost;
    @Column(length = 50)
    private String imei;
    private LocalDateTime createdAt = LocalDateTime.now();
}
