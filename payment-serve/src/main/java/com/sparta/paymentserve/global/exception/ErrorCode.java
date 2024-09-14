package com.sparta.paymentserve.global.exception;

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
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST,"잘못된 비밀번호입니다."),
    //상품 관련
    NOTFOUND_PRODUCT(HttpStatus.NOT_FOUND,"존재하지 않는 상품 id 입니다."),

    //위시리스트 관련
    NOTFOUND_WISHLIST(HttpStatus.NOT_FOUND,"존재하지 않는 위시리스트입니다."),
    NOTFOUND_WISHLISTITEM(HttpStatus.NOT_FOUND,"위시리스트에 존재하지 않는 상품입니다."),
    NOT_ACCESS_WISHLIST(HttpStatus.FORBIDDEN,"본인의 위시리스트가 아닌 위시리스트에는 접근할 수 없습니다."),

    //주문 관련
    NOTFOUND_ORDER(HttpStatus.NOT_FOUND,"존재하지 않는 주문입니다."),
    ORDER_CANNOT_BE_CANCEL(HttpStatus.BAD_REQUEST,"이미 배송중이므로 주문 취소가 불가능합니다."),
    ORDER_ALREADY_CANCEL(HttpStatus.BAD_REQUEST,"이미 주문 취소가 완료된 주문입니다."),
    CANNOT_BE_RETURN(HttpStatus.BAD_REQUEST,"반품이 불가능한 주문입니다.");
    private final HttpStatus status;
    private final String message;


}
