package com.sparta.productserve.global.exception;

import jakarta.persistence.EntityNotFoundException;

public class NotfoundResourceException extends EntityNotFoundException {
    private final ErrorCode errorCode;
    public NotfoundResourceException(final ErrorCode errorCode) {

        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
