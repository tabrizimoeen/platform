package org.platform.repair.entity;

import jakarta.persistence.*;
import lombok.*;
import org.platform.repair.enums.RepairStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "repair_order", schema = "repairs")
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

    private String deviceModel;

    @Column(columnDefinition = "TEXT")
    private String problemDescription;

    @Enumerated(EnumType.STRING)
    private RepairStatus status;

    private Long estimatedCost;

    private Long finalCost;
    private String imei;
    private LocalDateTime createdAt = LocalDateTime.now();
}
