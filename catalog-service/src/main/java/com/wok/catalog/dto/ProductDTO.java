package com.wok.catalog.dto;

import java.math.BigDecimal;

public record ProductDTO(
        Long id,
        String sku,
        String name,
        String description,
        String category,
        BigDecimal price,
        Integer stock
) {}
