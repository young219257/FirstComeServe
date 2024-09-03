package com.sparta.userserve.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //유저 관련
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복되는 이메일입니다."),
    VALIDATE_TOKEN(HttpStatus.BAD_REQUEST,"유효하지 않은 토큰입니다."),
    NOTFOUND_USER(HttpStatus.NOT_FOUND,"존재하지 않는 사용자입니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST,"잘못된 비밀번호입니다.");

    private final HttpStatus status;
    private final String message;


}
