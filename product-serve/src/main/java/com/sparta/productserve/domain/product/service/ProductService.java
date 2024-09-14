package com.sparta.productserve.domain.product.service;

import com.sparta.productserve.domain.product.dto.ProductListResponseDto;
import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.product.dto.StockResponseDto;
import com.sparta.productserve.domain.product.dto.StockUpdateDto;
import org.springframework.data.domain.Page;

public interface ProductService {

    Page<ProductListResponseDto> getAllProducts(int page, int size, String sortBy, boolean isAsc);

    ProductResponseDto getProduct(Long productId);

    void updateProductStock(StockUpdateDto stockUpdateDto);

    void undoProductStock(StockUpdateDto dto);

    StockResponseDto getProductStock(Long productId);
}
