package com.sparta.productserve.domain.product.service;


import com.sparta.productserve.domain.product.dto.ProductListResponseDto;
import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.product.dto.ProductStockDto;
import com.sparta.productserve.domain.product.dto.ProductStockUpdateDto;
import com.sparta.productserve.domain.product.entity.Product;
import com.sparta.productserve.domain.product.repository.ProductRepository;
import com.sparta.productserve.global.exception.ErrorCode;
import com.sparta.productserve.global.exception.NotfoundResourceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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

        Product product=findProductById(productId);

        return ProductResponseDto.of(product);
    }

    @Override
    @Transactional
    public void updateProductStock(ProductStockUpdateDto productStockUpdateDto) {

        log.info(String.valueOf(productStockUpdateDto.getQuantity()));

        Product product=findProductById(productStockUpdateDto.getProductId());
        int newStock=product.getStockQuantity()-productStockUpdateDto.getQuantity();
//
//        if(newStock<0){
//            throw new
//        }
        product.updateStock(newStock);

        log.info(String.valueOf(product.getStockQuantity()));
    }

    @Override
    @Transactional
    public void undoProductStock(ProductStockUpdateDto productStockUpdateDto) {
        Product product=findProductById(productStockUpdateDto.getProductId());
        product.updateStock(product.getStockQuantity()+productStockUpdateDto.getQuantity());

    }

    @Override
    public ProductStockDto getProductStock(Long productId) {
        Product product=findProductById(productId);
        return ProductStockDto.builder().stock(product.getStockQuantity()).build();
    }

    private Product findProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT));

    }
}
