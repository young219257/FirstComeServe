package com.sparta.productserve.domain.product.service;


import com.sparta.productserve.domain.product.dto.ProductListResponseDto;
import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.product.dto.StockResponseDto;
import com.sparta.productserve.domain.product.dto.StockUpdateDto;
import com.sparta.productserve.domain.product.entity.Product;
import com.sparta.productserve.domain.product.entity.Stock;
import com.sparta.productserve.domain.product.repository.ProductRepository;
import com.sparta.productserve.domain.product.repository.StockRepository;
import com.sparta.productserve.global.exception.ErrorCode;
import com.sparta.productserve.global.exception.InsufficientStockException;
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
    private final StockRepository stockRepository;

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

        return ProductResponseDto.from(product);
    }

    @Override
    @Transactional
    public void updateProductStock(StockUpdateDto stockUpdateDto) {

        Stock stock=findStockByProductId(stockUpdateDto.getProductId());
        int newQuantity=stock.getQuantity()- stockUpdateDto.getQuantity();

        //재고 부족일 때 주문 불가
        if(newQuantity<0){
            throw new InsufficientStockException(ErrorCode.INSUFFICIENT_STOCK);
        }
        stock.updateQuantity(newQuantity);

    }

    @Override
    @Transactional
    public void undoProductStock(StockUpdateDto stockUpdateDto) {

        Stock newStock=findStockByProductId(stockUpdateDto.getProductId());
        int newQuantity=newStock.getQuantity()+ stockUpdateDto.getQuantity();
        newStock.updateQuantity(newQuantity);

    }

    @Override
    public StockResponseDto getProductStock(Long productId) {
        Product product=findProductById(productId);
        Stock stock=findStockByProductId(productId);
        return StockResponseDto.builder().
                productId(productId).
                productName(product.getProductName()).
                stock(stock.getQuantity()).build();
    }

    private Stock findStockByProductId(Long productId) {
        return stockRepository.findById(productId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT));
    }
    private Product findProductById(Long productId) {
        log.info("Looking for product with id: {}", productId);
        return productRepository.findById(productId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT));

    }
}
