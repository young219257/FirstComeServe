package com.sparta.paymentserve.global.exception;


public class InvalidateTokenException extends RuntimeException {
    private ErrorCode errorCode;
    public InvalidateTokenException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
