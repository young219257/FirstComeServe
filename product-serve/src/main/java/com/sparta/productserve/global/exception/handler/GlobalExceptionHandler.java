package com.sparta.productserve.global.exception.handler;

import com.sparta.productserve.global.exception.*;
import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity exception(Exception ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity DuplicateResourceException(DuplicateResourceException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotfoundResourceException.class)
    public ResponseEntity NotFoundResourceException(NotfoundResourceException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity InvalidOrderStatusException(InvalidOrderStatusException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidReturnException.class)
    public ResponseEntity InvalidReturnException(InvalidReturnException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity InsufficientStockException(InsufficientStockException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);
    }
}
