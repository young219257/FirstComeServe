package com.sparta.userserve.global.exception;

public class InvalidReturnException extends RuntimeException {
    private final ErrorCode errorCode;
    public InvalidReturnException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }
}
