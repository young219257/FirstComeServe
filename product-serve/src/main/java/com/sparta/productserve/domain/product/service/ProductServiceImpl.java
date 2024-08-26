package com.sparta.productserve.domain.product.service;


import com.sparta.productserve.domain.product.dto.ProductListResponseDto;
import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.product.entity.Product;
import com.sparta.productserve.domain.product.repository.ProductRepository;
import com.sparta.productserve.global.exception.ErrorCode;
import com.sparta.productserve.global.exception.NotfoundResourceException;
import com.sparta.productserve.domain.product.dto.ProductListResponseDto;
import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.product.entity.Product;
import com.sparta.productserve.domain.product.repository.ProductRepository;
import com.sparta.productserve.global.exception.ErrorCode;
import com.sparta.productserve.global.exception.NotfoundResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
        // Product 객체를 ProductListResponseDto로 변환
        return products.map(ProductListResponseDto::from);
    }

    @Override
    public ProductResponseDto getProduct(Long productId) {

        Product product = productRepository.findById(productId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT));

        ProductResponseDto productResponseDto = ProductResponseDto.of(product);
        return productResponseDto;
    }
}
