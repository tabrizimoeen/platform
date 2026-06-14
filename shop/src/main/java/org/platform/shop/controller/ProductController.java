package org.platform.shop.controller;


import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.product.CreateProductRequest;
import org.platform.shop.dto.product.ProductResponse;
import org.platform.shop.dto.product.UpdateProductRequest;
import org.platform.shop.entity.Product;
import org.platform.shop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product create(@RequestBody CreateProductRequest req) {
        return productService.create(req);
    }

    @GetMapping
    public Page<Product> getAll( @RequestParam(defaultValue = "0")
                                     int page,

                                 @RequestParam(defaultValue = "10")
                                     int size) {
        return productService.getAll(page,size);
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.getById(id);
    }

    @GetMapping("/search")
    public List<Product> search(@RequestParam String q) {
        return productService.search(q);
    }
    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody UpdateProductRequest request
    ) {
        return productService.update(id, request);
    }
}