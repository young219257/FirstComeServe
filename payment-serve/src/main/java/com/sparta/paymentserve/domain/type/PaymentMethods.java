package com.sparta.paymentserve.domain.type;


import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaymentMethods {
    CARDS("카드"),
    ACCOUNT_TRANSFER("계좌 이체");

    private final String description;



    @JsonCreator
    public static PaymentMethods from(String value) {
        for (PaymentMethods method : PaymentMethods.values()) {
            if (method.getDescription().equals(value)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}