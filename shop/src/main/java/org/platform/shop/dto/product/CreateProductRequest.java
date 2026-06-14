package org.platform.shop.dto.product;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateProductRequest {

    private Long categoryId;

    private String name;

    private String slug;

    private String description;

    private BigDecimal price;

    private Integer inventory;

    private List<String> images;
}