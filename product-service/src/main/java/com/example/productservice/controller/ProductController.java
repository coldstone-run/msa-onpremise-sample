package com.example.productservice.controller;

import com.example.common.dto.ProductDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    List<ProductDto> products = List.of(
            new ProductDto("1", "MacBook Pro", 3500000),
            new ProductDto("2", "iPad Air", 900000),
            new ProductDto("3", "Galaxy S25 Ultra", 1500000)
    );

    // 전체 상품 보기
    @GetMapping
    public List<ProductDto> getProducts() {
        return products;
    }

    // 상품 상세 보기
    @GetMapping("/{productId}")
    public ProductDto getProductById(@PathVariable String productId) {
        return products.get(Integer.parseInt(productId) - 1);
    }

    // 상품 등록

    // 상품 정보 수정

    // 상품 삭제
}
