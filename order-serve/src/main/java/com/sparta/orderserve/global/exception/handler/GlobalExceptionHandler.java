package com.sparta.orderserve.global.exception.handler;

import com.sparta.orderserve.global.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity exception(Exception ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(NotfoundResourceException.class)
    public ResponseEntity NotFoundResourceException(NotfoundResourceException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.NOT_FOUND);
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity AccessDeniedException(AccessDeniedException ex) {
//        RestApiException restApiException = new RestApiException(HttpStatus.FORBIDDEN.value(), ex.getMessage());
//        return new ResponseEntity<>(restApiException, HttpStatus.FORBIDDEN);
//
//    }
//
//    @ExceptionHandler(UsernameNotFoundException.class)
//    public ResponseEntity UsernameNotFoundException(UsernameNotFoundException ex) {
//        RestApiException restApiException = new RestApiException(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
//        return new ResponseEntity<>(restApiException, HttpStatus.UNAUTHORIZED);
//    }

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

}
