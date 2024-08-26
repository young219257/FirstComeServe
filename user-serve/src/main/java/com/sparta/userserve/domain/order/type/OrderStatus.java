package com.sparta.userserve.domain.order.type;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {

    ORDER_START("주문 완료"),
    ORDER_CANCEL("주문 취소 완료"),
    ORDER_COMPLETE("거래 확정"),
    SIGN_RETURN("반품 신청"),
    RETURN("반품 완료");
    private final String description;

}
