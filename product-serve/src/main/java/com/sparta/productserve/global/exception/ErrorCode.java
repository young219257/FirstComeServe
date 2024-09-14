package com.sparta.productserve.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
   //상품 관련
    NOTFOUND_PRODUCT(HttpStatus.NOT_FOUND,"존재하지 않는 상품 id 입니다."),

    //위시리스트 관련
    NOTFOUND_WISHLIST(HttpStatus.NOT_FOUND,"존재하지 않는 위시리스트입니다."),
    NOTFOUND_WISHLISTITEM(HttpStatus.NOT_FOUND,"위시리스트에 존재하지 않는 상품입니다."),
    NOT_ACCESS_WISHLIST(HttpStatus.FORBIDDEN,"본인의 위시리스트가 아닌 위시리스트에는 접근할 수 없습니다."),
    INSUFFICIENT_STOCK(HttpStatus.CONFLICT,"재고가 부족하여 주문할 수 없습니다.");
    private final HttpStatus status;
    private final String message;


}
