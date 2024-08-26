package com.sparta.productserve.global.exception;

public class InvalidOrderStatusException extends RuntimeException {
    private final ErrorCode errorCode;
    public InvalidOrderStatusException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
