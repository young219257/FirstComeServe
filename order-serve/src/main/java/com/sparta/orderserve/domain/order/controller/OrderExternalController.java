package com.sparta.orderserve.domain.order.controller;

import com.sparta.orderserve.domain.order.dto.OrderDto;
import com.sparta.orderserve.domain.order.service.OrderService;
import com.sparta.orderserve.global.exception.handler.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/external/order")
public class OrderExternalController {

    private final OrderService orderService;


    @GetMapping("/{orderId}")
    public ApiResponse<OrderDto> getOrder(@PathVariable Long orderId) {
        OrderDto orderDto =orderService.getOrderInfo(orderId);
        return ApiResponse.ok(200,"주문 정보를 payment 서비스 전달에 성공하였습니다.", orderDto);
    }

}
