package com.wok.catalog.controller;

import com.wok.catalog.dto.ProductDTO;
import com.wok.catalog.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        // Virtual thread handles this request
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getProductsByCategory(
            @RequestParam String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(productService.getProductsByCategory(category, page, size));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id,
                                                    @Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProduct(id, productDTO));
    }
}
