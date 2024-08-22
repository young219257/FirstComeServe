package com.sparta.firstseversystem.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //유저 관련
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복되는 이메일입니다."),
    VALIDATE_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰입니다."),

    NOTFOUND_PRODUCT(HttpStatus.NOT_FOUND,"존재하지 않는 상품 id 입니다.");
    private final HttpStatus status;
    private final String message;


}
