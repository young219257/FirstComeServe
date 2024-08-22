package com.sparta.firstseversystem.product.service;


import com.sparta.firstseversystem.global.exception.ErrorCode;
import com.sparta.firstseversystem.global.exception.NotfoundResourceException;
import com.sparta.firstseversystem.product.dto.ProductListResponseDto;
import com.sparta.firstseversystem.product.dto.ProductResponseDto;
import com.sparta.firstseversystem.product.entity.Product;
import com.sparta.firstseversystem.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Page<ProductListResponseDto> getAllProducts(int page, int size, String sortBy, boolean isAsc) {
        // 정렬 방향 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Product> products = productRepository.findAll(pageable);

        return products.map(product -> new ProductListResponseDto());
    }

    @Override
    public ProductResponseDto getProduct(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT));

        ProductResponseDto productResponseDto = ProductResponseDto.of(product);
        return productResponseDto;
    }
}
