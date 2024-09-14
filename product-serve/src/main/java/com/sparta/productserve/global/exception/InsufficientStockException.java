package com.sparta.productserve.global.exception;

public class InsufficientStockException extends RuntimeException {

    private final ErrorCode errorCode;

    public InsufficientStockException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode=errorCode;
    }
}
