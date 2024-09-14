package com.sparta.paymentserve.domain.controller;

import com.sparta.paymentserve.domain.dto.PaymentRequestDto;
import com.sparta.paymentserve.domain.service.PaymentService;
import com.sparta.paymentserve.global.exception.handler.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;


    /**결제 진입 api
     * 결제 정보를 table에 저장하는 api
     * @param userId
     * @param paymentRequestDto
     * @return ApiResponse
     */
    @PostMapping
    public ApiResponse createdPayment(@RequestHeader("userId") String userId, @RequestBody PaymentRequestDto paymentRequestDto) {

        //20% 확률로 실패(장애 상황 연출)
        if(new Random().nextInt(100)<20){
            return ApiResponse.error(500,"내부 서버 오류로 인해 실패하였습니다.");
        }
        paymentService.createdPayment(Long.valueOf(userId), paymentRequestDto);
        return ApiResponse.ok(200, "결제 진입에 성공하였습니다.");
    }

    @PutMapping("/{paymentId}")
    public ApiResponse approvedPayment(@PathVariable("paymentId") Long paymentId, @RequestHeader("userId") String userId) {

        //20% 확률로 실패
        if(new Random().nextInt(100)<20){
            return ApiResponse.error(500,"내부 서버 오류로 인해 실패하였습니다.");
        }
        paymentService.approvedPayment(paymentId,userId);
        return ApiResponse.ok(200,"결제가 완료되었습니다.");
    }

}
