package com.sparta.firstseversystem.domain.order.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {

    ORDER_START("주문 완료"),
    ORDER_CANCEL("주문 취소"),
    ORDER_COMPLETE("거래 확정");

    private final String description;

}
