package com.sparta.orderserve.domain.order.client;


import com.sparta.orderserve.domain.order.dto.ProductDto;
import com.sparta.orderserve.domain.order.dto.ProductUpdateRequestDto;
import com.sparta.orderserve.domain.order.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderClient {

    private final WebClient webClient;


    //user 정보 가져오는 메소드(webClient)
    public  Mono<UserDto> getUserById(Long userId){
        return webClient.get()
                .uri("http://localhost:8080/api/external/user/"+userId)
                .retrieve()
                .bodyToMono(UserDto.class);
    }


    //상품 가져오는 메소드(webClient)
    public Mono<ProductDto> getProductById(Long productId) {
        return webClient.get()
                .uri("http://localhost:8080/api/external/product/"+productId)
                .retrieve()
                .bodyToMono(ProductDto.class);
    }


    //상품 제고 업데이트 하는 메소드
    public void updateProductStock(ProductDto productDto,int newStockQuantity){
        webClient.put().
                uri("http://localhost:8080/product/"+productDto.getProductId())
                .bodyValue(new ProductUpdateRequestDto(newStockQuantity))
                .retrieve()
                .toBodilessEntity();
    }
}
