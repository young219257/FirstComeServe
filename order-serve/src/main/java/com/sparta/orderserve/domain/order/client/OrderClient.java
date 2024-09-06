package com.sparta.orderserve.domain.order.client;


import com.sparta.orderserve.domain.order.dto.ProductDto;
import com.sparta.orderserve.domain.order.dto.ProductUpdateRequestDto;
import com.sparta.orderserve.domain.order.dto.UserDto;
import com.sparta.orderserve.global.exception.handler.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.lang.reflect.Type;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderClient {

    private final WebClient webClient;


    //user 정보 가져오는 메소드(webClient)
    public  Mono<ApiResponse<UserDto>> getUserById(Long userId){
        return webClient.get()
                .uri("http://localhost:8080/api/external/user/"+userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserDto>>() {} );
    }


    // 상품을 가져오는 메소드
    public Mono<ApiResponse<ProductDto>> getProductById(Long productId) {
        return webClient.get()
                .uri("http://localhost:8082/api/external/product/{productId}", productId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<ProductDto>>() {});
    }

}
