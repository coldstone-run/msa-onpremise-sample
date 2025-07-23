package com.example.orderservice.client;

import com.example.common.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public List<ProductDto> getProducts() {
        return List.of(
                new ProductDto("fallback", "Cannot Read Products List", 0)
        );
    }
}
