package com.sparta.orderserve.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    //주문 관련
    NOTFOUND_ORDER(HttpStatus.NOT_FOUND,"존재하지 않는 주문입니다."),
    ORDER_CANNOT_BE_CANCEL(HttpStatus.BAD_REQUEST,"이미 배송중이므로 주문 취소가 불가능합니다."),
    ORDER_ALREADY_CANCEL(HttpStatus.BAD_REQUEST,"이미 주문 취소가 완료된 주문입니다."),
    CANNOT_BE_RETURN(HttpStatus.BAD_REQUEST,"반품이 불가능한 주문입니다."),
    EMPTY_ORDERITEM(HttpStatus.NOT_FOUND,"주문할 상품을 담아주세요.");
    private final HttpStatus status;
    private final String message;


}
