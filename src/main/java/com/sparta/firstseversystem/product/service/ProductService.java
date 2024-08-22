package com.sparta.firstseversystem.product.service;

import com.sparta.firstseversystem.product.dto.ProductListResponseDto;
import com.sparta.firstseversystem.product.dto.ProductResponseDto;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductListResponseDto> getAllProducts(int page, int size, String sortBy, boolean isAsc);

    ProductResponseDto getProduct(Long productId);
}
