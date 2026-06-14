package org.platform.shop.dto.product;

import lombok.Data;
import org.platform.shop.enums.ProductStatus;

import java.math.BigDecimal;

@Data
public class UpdateProductRequest {

    private Long categoryId;

    private String name;

    private String slug;

    private String description;

    private BigDecimal price;

    private Integer inventory;

    private Boolean active;
    private ProductStatus status;
}