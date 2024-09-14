package com.sparta.paymentserve.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    NOTFOUND_PAYMENT(HttpStatus.NOT_FOUND,"존재하지 않는 결제 정보입니다."),
    INVALID_PAYMENT(HttpStatus.BAD_REQUEST,"이미 결제가 된 주문입니다.");

    private final HttpStatus status;
    private final String message;


}
