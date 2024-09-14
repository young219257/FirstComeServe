package com.sparta.orderserve.domain.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.orderserve.domain.order.dto.OrderDto;
import com.sparta.orderserve.domain.order.dto.OrderRequestDto;
import com.sparta.orderserve.domain.order.dto.OrderResponseDto;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;

public interface OrderService {
    void createOrder(Long userId, OrderRequestDto orderRequestDto) throws JsonProcessingException;

    @Transactional
    void completeOrder(Long orderId) throws JsonProcessingException;

    Page<OrderResponseDto> getAllOrders(Long userId, int page, int size, String sortBy, boolean isAsc);

    void deleteOrder(Long userId, Long orderId) throws JsonProcessingException;

    void returnOrder(Long userId, Long orderId);

    OrderDto getOrderInfo(Long orderId);
}
