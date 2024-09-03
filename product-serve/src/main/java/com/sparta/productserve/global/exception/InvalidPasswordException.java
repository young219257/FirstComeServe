package com.sparta.productserve.global.exception;

public class InvalidPasswordException extends RuntimeException {
    ErrorCode errorCode;
    public InvalidPasswordException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }
}
