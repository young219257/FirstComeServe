package com.sparta.orderserve.domain.product.service;

import com.sparta.orderserve.domain.product.dto.ProductListResponseDto;
import com.sparta.orderserve.domain.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductListResponseDto> getAllProducts(int page, int size, String sortBy, boolean isAsc);

    ProductResponseDto getProduct(Long productId);
}
