package com.sparta.firstseversystem.global.exception;

import lombok.RequiredArgsConstructor;


public class UnAuthorizedAccessException extends RuntimeException {
    private final ErrorCode errorCode;
    public UnAuthorizedAccessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

