package com.sparta.firstseversystem.global.exception.handler;

import com.sparta.firstseversystem.global.exception.DuplicateResourceException;
import com.sparta.firstseversystem.global.exception.InvalidateTokenException;
import com.sparta.firstseversystem.global.exception.NotfoundResourceException;
import com.sparta.firstseversystem.global.exception.RestApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

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

    @ExceptionHandler(InvalidateTokenException.class)
    public ResponseEntity InvalidateTokenException(InvalidateTokenException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(NotfoundResourceException.class)
    public ResponseEntity NotFoundResourceException(NotfoundResourceException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity AccessDeniedException(AccessDeniedException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity UsernameNotFoundException(UsernameNotFoundException ex) {
        RestApiException restApiException = new RestApiException(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(restApiException, HttpStatus.UNAUTHORIZED);
    }
}
