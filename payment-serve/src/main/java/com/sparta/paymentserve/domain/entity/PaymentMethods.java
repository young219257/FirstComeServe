package com.sparta.paymentserve.domain.entity;


import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public enum PaymentMethods {

    CARDS("카드"),
    ACCOUNT_TRANSFER("계좌이체");

    private String status;
}
