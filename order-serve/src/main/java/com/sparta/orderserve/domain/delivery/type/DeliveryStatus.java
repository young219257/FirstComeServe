package com.sparta.orderserve.domain.delivery.type;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DeliveryStatus {
    READY_DELIVERY("배송 준비 중"),
    DELIVERYING("배송 중"),
    COMPLETE_DELIVERY("배송 완료");
    private final String description;
}
