package org.platform.shop.controller.admin;

import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.product.CreateProductRequest;
import org.platform.shop.dto.product.UpdateProductRequest;
import org.platform.shop.entity.Product;
import org.platform.shop.service.ProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request
    ) {

        return productService.update(
                id,
                request
        );
    }
    @PostMapping
    public Product create(
            @RequestBody CreateProductRequest request
    ) {

        return productService.create(request);
    }
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {

        productService.delete(id);
    }
}