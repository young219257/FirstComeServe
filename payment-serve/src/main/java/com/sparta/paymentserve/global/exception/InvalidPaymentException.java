package com.sparta.paymentserve.global.exception;

public class InvalidPaymentException extends RuntimeException {
    private final ErrorCode errorCode;
    public InvalidPaymentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;


    }
}
