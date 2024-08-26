package com.sparta.orderserve.global.exception;


public class UnAuthorizedAccessException extends RuntimeException {
    private final ErrorCode errorCode;
    public UnAuthorizedAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

