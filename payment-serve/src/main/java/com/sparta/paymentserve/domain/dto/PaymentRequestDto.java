package com.sparta.paymentserve.domain.dto;

import com.sparta.paymentserve.domain.type.PaymentMethods;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {

    private Long orderId;
    private PaymentMethods paymentMethods;


}
