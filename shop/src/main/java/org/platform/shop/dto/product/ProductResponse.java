package org.platform.shop.dto.product;

import java.math.BigDecimal;
import java.util.List;

public class ProductResponse {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private BigDecimal price;

    private Integer inventory;

    private String categoryName;

    private List<String> images;
}