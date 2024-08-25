package com.sparta.firstseversystem.domain.order.service;

import com.sparta.firstseversystem.domain.order.dto.OrderRequestDto;
import com.sparta.firstseversystem.domain.order.dto.OrderResponseDto;
import com.sparta.firstseversystem.domain.user.entity.User;
import org.springframework.data.domain.Page;

public interface OrderService {
    void createOrder(User user, OrderRequestDto orderRequestDto);

    Page<OrderResponseDto> getAllOrders(User user, int page, int size, String sortBy, boolean isAsc);

    void deleteOrder(User user, Long orderId);

    void returnOrder(User user, Long orderId);
}
