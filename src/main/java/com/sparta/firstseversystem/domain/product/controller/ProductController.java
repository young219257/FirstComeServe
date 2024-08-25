package com.sparta.firstseversystem.domain.product.controller;


import com.sparta.firstseversystem.domain.product.dto.ProductListResponseDto;
import com.sparta.firstseversystem.domain.product.dto.ProductResponseDto;
import com.sparta.firstseversystem.domain.product.service.ProductService;
import com.sparta.firstseversystem.global.exception.handler.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**상품 목록 조회**/
    @GetMapping
    public ApiResponse<Page<ProductListResponseDto>> getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                                                    @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                                    @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {
        Page<ProductListResponseDto> products=productService.getAllProducts(page, size, sortBy, isAsc);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품 목록 조회에 성공하셨습니다.", products);
    }

    /**상품 상세 조회**/
    @GetMapping("/{productId}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable("productId") Long productId) {
        ProductResponseDto product=productService.getProduct(productId);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품 상세 조회에 성공하셨습니다.",product);
    }
}
