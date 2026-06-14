package org.platform.shop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.platform.shop.dto.product.CreateProductRequest;
import org.platform.shop.dto.product.UpdateProductRequest;
import org.platform.shop.entity.Category;
import org.platform.shop.entity.Product;
import org.platform.shop.entity.ProductImage;
import org.platform.shop.enums.ProductStatus;
import org.platform.shop.exception.BusinessException;
import org.platform.shop.repository.CategoryRepository;
import org.platform.shop.repository.ProductImageRepository;
import org.platform.shop.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository imageRepository;

    public Product create(CreateProductRequest req) {

        Category category = categoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName(req.getName());
        product.setSlug(req.getSlug());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setInventory(req.getInventory());
        product.setCategory(category);
        product.setActive(true);
        product.setStatus(ProductStatus.ACTIVE);
        Product saved = productRepository.save(product);

        if (req.getImages() != null) {
            int i = 0;
            for (String url : req.getImages()) {

                ProductImage img = new ProductImage();
                img.setProduct(saved);
                img.setImageUrl(url);
                img.setSortOrder(i++);

                imageRepository.save(img);
            }
        }

        return saved;
    }

    public Page<Product> getAll(int page, int size) {
        return productRepository.findByActiveTrue(PageRequest.of(page,size));
    }

    public Product getById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> search(String q) {
        return productRepository.findByNameContainingIgnoreCase(q);
    }
    @Transactional
    public Product update(
            Long id,
            UpdateProductRequest request
    ) {

        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new BusinessException(
                                        "Product not found"
                                )
                        );

        Category category =
                categoryRepository
                        .findById(
                                request.getCategoryId()
                        )
                        .orElseThrow();

        product.setCategory(category);
        product.setName(request.getName());
        product.setSlug(request.getSlug());
        product.setDescription(
                request.getDescription()
        );
        product.setPrice(request.getPrice());
        product.setInventory(
                request.getInventory()
        );
        product.setActive(
                request.getActive()
        );product.setStatus(request.getStatus());

        return productRepository.save(product);
    }
    @Transactional
    public void delete(Long id) {

        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new BusinessException(
                                        "Product not found"
                                )
                        );

        productRepository.delete(product);
    }
}