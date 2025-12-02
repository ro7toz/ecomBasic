package com.wok.catalog.service;

import com.wok.catalog.dto.ProductDTO;
import com.wok.catalog.entity.Product;
import com.wok.catalog.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));
    }

    @Cacheable(value = "products_by_category", key = "#category + ':' + #page")
    public Page<ProductDTO> getProductsByCategory(String category, int page, int size) {
        var pageable = PageRequest.of(page, size);
        return productRepository.findByCategory(category, pageable)
                .map(this::toDTO);
    }

    @CacheEvict(value = {"products", "products_by_category"}, allEntries = true)
    public ProductDTO createProduct(ProductDTO dto) {
        var product = new Product();
        product.setSku(dto.sku());
        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setCategory(dto.category());
        product.setPrice(dto.price());
        product.setStock(dto.stock());

        var saved = productRepository.save(product);
        return toDTO(saved);
    }

    @CacheEvict(value = {"products", "products_by_category"}, allEntries = true)
    public ProductDTO updateProduct(Long id, ProductDTO dto) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + id));

        product.setName(dto.name());
        product.setDescription(dto.description());
        product.setPrice(dto.price());
        product.setStock(dto.stock());

        var updated = productRepository.save(product);
        return toDTO(updated);
    }

    @Transactional(readOnly = true)
    public void checkStock(Long productId, int quantity) {
        Optional<Product> product = productRepository.findById(productId);

        if (product.isEmpty()) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }

        // Java 21 Pattern Matching
        if (product.get() instanceof Product p && p.getStock() < quantity) {
            throw new IllegalStateException("Insufficient stock for product: " + productId);
        }
    }

    private ProductDTO toDTO(Product p) {
        return new ProductDTO(
                p.getId(),
                p.getSku(),
                p.getName(),
                p.getDescription(),
                p.getCategory(),
                p.getPrice(),
                p.getStock()
        );
    }
}
