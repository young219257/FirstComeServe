package com.sparta.paymentserve.domain.type;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@RequiredArgsConstructor
public enum PaymentStatus {

    PAYMENT_READY("결제 대기"),
    PAYMENT_APPROVE("결제 승인"),
    PAYMENT_CANCEL("결제 취소");

    private String description;
}
