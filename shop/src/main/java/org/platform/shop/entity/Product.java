package org.platform.shop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.platform.shop.enums.ProductStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(
        name = "products",
        schema = "shops",
        indexes = {
                @Index(name = "idx_product_category", columnList = "category_id"),
                @Index(name = "idx_product_slug", columnList = "slug")
        }
)
public class Product extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer inventory;

    @Column(nullable = false)
    private Boolean active = true;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    @Column(nullable = false)
    private Integer reservedInventory = 0;
    @Transient
    public Integer getAvailableInventory() {

        return inventory - reservedInventory;

    }
}