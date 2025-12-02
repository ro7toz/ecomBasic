package com.wok.catalog.service;

import com.wok.catalog.entity.Product;
import com.wok.catalog.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testGetProductById_Success() {
        var product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setPrice(BigDecimal.valueOf(50000));

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        var result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals("Laptop", result.name());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> productService.getProductById(1L));
    }

    @Test
    void testCheckStock_InsufficientStock() {
        var product = new Product();
        product.setStock(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(IllegalStateException.class,
                () -> productService.checkStock(1L, 20));
    }
}
