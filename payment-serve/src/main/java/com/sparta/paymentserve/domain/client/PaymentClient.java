package com.sparta.paymentserve.domain.client;

import com.sparta.paymentserve.domain.dto.OrderInfoDto;
import com.sparta.paymentserve.global.exception.handler.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentClient {

    private final WebClient webClient;

    //order 정보를 가져오는 메소드
    //필요한 정보
    //주문 금액, 주문자, 주문 상태(주문 완료일 때만 -> payment 생성)
    public Mono<ApiResponse<OrderInfoDto>> getOrderInfo(Long orderId){
        return webClient.get()
                .uri("http://localhost:8081/api/external/order/"+orderId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<OrderInfoDto>>() {});
    }

}
