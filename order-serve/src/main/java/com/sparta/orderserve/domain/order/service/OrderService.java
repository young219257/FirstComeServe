package com.sparta.orderserve.domain.order.service;

import com.sparta.orderserve.domain.order.dto.OrderRequestDto;
import com.sparta.orderserve.domain.order.dto.OrderResponseDto;
import org.springframework.data.domain.Page;

public interface OrderService {
    void createOrder(Long userId, OrderRequestDto orderRequestDto);

    Page<OrderResponseDto> getAllOrders(Long userId, int page, int size, String sortBy, boolean isAsc);

    void deleteOrder(Long userId, Long orderId);

    void returnOrder(Long userId, Long orderId);
}
