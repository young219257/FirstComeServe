package com.sparta.productserve.domain.product.controller;


import com.sparta.productserve.domain.product.dto.ProductListResponseDto;
import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.product.dto.StockResponseDto;
import com.sparta.productserve.domain.product.service.ProductService;
import com.sparta.productserve.global.exception.handler.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**상품 목록 조회**/
    @GetMapping("/products")
    public ApiResponse<Page<ProductListResponseDto>> getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                                                    @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                                    @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {
        Page<ProductListResponseDto> products=productService.getAllProducts(page, size, sortBy, isAsc);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품 목록 조회에 성공하셨습니다.", products);
    }

    /**상품 상세 조회**/
    @GetMapping("/products/{productId}")
    public ApiResponse<ProductResponseDto> getProduct(@PathVariable("productId") Long productId) {
        ProductResponseDto product=productService.getProduct(productId);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품 상세 조회에 성공하셨습니다.",product);
    }

    /** 상품 수량 조회**/
    @GetMapping("/products/{productId}/stock")
    public ApiResponse<StockResponseDto> getProductStock(@PathVariable("productId") Long productId){
        StockResponseDto stockResponseDto =productService.getProductStock(productId);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품 수량 조회에 성공하셨습니다.", stockResponseDto);

    }
}
