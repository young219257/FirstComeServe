package com.sparta.paymentserve.domain.service;

import com.sparta.paymentserve.domain.dto.PaymentRequestDto;

public interface PaymentService {
    void createdPayment(Long userId, PaymentRequestDto paymentRequestDto);

    void approvedPayment(Long paymentId, String userId);

    void canceledPayment(Long orderId);
}
