package com.sparta.productserve.domain.product.controller;

import com.sparta.productserve.domain.product.dto.ProductResponseDto;
import com.sparta.productserve.domain.product.service.ProductService;
import com.sparta.productserve.global.exception.handler.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/external/product")
@RequiredArgsConstructor
public class ProductExternalController {

    private final ProductService productService;


    @GetMapping("/{productId}")
    public ApiResponse<ProductResponseDto> sendProductInfo(@PathVariable("productId") Long productId) {
        ProductResponseDto product=productService.getProduct(productId);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품 상세 조회에 성공하셨습니다.",product);
    }
}
