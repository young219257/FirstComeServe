package com.sparta.firstseversystem.product.controller;


import com.sparta.firstseversystem.global.exception.handler.dto.ApiResponse;
import com.sparta.firstseversystem.product.dto.ProductListResponseDto;
import com.sparta.firstseversystem.product.dto.ProductResponseDto;
import com.sparta.firstseversystem.product.entity.Product;
import com.sparta.firstseversystem.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<ProductListResponseDto>> getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                                                    @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                                    @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {
        Page<ProductListResponseDto> products=productService.getAllProducts(page, size, sortBy, isAsc);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품 목록 조회에 성공하셨습니다.", products);
    }
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable("productId") Long productId) {
        ProductResponseDto product=productService.getProduct(productId);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품 상세 조회에 성공하셨습니다.",product);
    }
}
