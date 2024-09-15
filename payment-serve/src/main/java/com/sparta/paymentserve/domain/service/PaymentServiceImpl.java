package com.sparta.paymentserve.domain.service;

import com.sparta.paymentserve.domain.client.PaymentClient;

import com.sparta.paymentserve.domain.dto.OrderDto;
import com.sparta.paymentserve.domain.dto.PaymentRequestDto;
import com.sparta.paymentserve.domain.entity.Payment;
import com.sparta.paymentserve.domain.producer.PaymentProducer;
import com.sparta.paymentserve.domain.type.PaymentStatus;
import com.sparta.paymentserve.domain.repository.PaymentRepository;
import com.sparta.paymentserve.global.exception.ErrorCode;
import com.sparta.paymentserve.global.exception.NotfoundResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentClient paymentClient;
    private final PaymentProducer paymentProducer;

    @Override
    public void createdPayment(Long userId, PaymentRequestDto paymentRequestDto) {

        /**주문 정보 가져오기**/
        OrderDto orderInfoDto= paymentClient.getOrderInfo(paymentRequestDto.getOrderId()).block().getData();

//        if(!orderInfoDto.getOrderStatus().equals("주문 대기")){
//            throw new InvalidPaymentException(ErrorCode.INVALID_PAYMENT);
//        }
        int deliveryPrice=0;
        if(orderInfoDto.getTotalOrderPrice()<30000){
            deliveryPrice=2500;
        }
        Payment payment=Payment.builder()
                .orderId(paymentRequestDto.getOrderId())
                .paymentMethod(paymentRequestDto.getPaymentMethods())
                .status(PaymentStatus.PAYMENT_READY)
                .orderPrice(orderInfoDto.getTotalOrderPrice())
                .deliveryPrice(deliveryPrice)
                .totalPaymentAmount(orderInfoDto.getTotalOrderPrice()+deliveryPrice)
                .build();

        paymentRepository.save(payment);

    }

    @Override
    @Transactional
    public void approvedPayment(Long paymentId, String userId) {
        Payment payment=paymentRepository.findById(paymentId).
                orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_PAYMENT));

        payment.approvedPayment();

        //결제 완료 이벤트 발행
        paymentProducer.approvedPayment(payment.getOrderId());
    }

    //paymentStatus 변경
    public void canceledPayment(Long orderId) {
        Payment payment=paymentRepository.findByOrderId(orderId).
                orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_PAYMENT));

        payment.updatePaymentStatus(PaymentStatus.PAYMENT_CANCEL);

    }


}
