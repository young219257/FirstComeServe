package com.sparta.productserve.domain.product.service;

import com.sparta.productserve.domain.product.dto.ProductListResponseDto;
import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.product.dto.ProductStockDto;
import com.sparta.productserve.domain.product.dto.ProductStockUpdateDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductListResponseDto> getAllProducts(int page, int size, String sortBy, boolean isAsc);

    ProductResponseDto getProduct(Long productId);

    void updateProductStock(ProductStockUpdateDto productStockUpdateDto);

    void undoProductStock(ProductStockUpdateDto dto);

    ProductStockDto getProductStock(Long productId);
}
