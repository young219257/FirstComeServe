package com.sparta.firstseversystem.domain.order.controller;
import com.sparta.firstseversystem.domain.order.dto.OrderRequestDto;
import com.sparta.firstseversystem.domain.order.dto.OrderResponseDto;
import com.sparta.firstseversystem.domain.order.service.OrderService;
import com.sparta.firstseversystem.global.exception.handler.dto.ApiResponse;
import com.sparta.firstseversystem.global.security.service.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;


    /** 상품 페이지에서 해당 상품 주문하는 메소드 **/
    @PostMapping
    public ApiResponse createOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   @RequestBody OrderRequestDto orderRequestDto) {
        orderService.createOrder(customUserDetails.getUser(),orderRequestDto);
        return ApiResponse.ok(HttpStatus.OK.value(), "상품 주문에 완료하였습니다.");
    }

    /**주문 정보 조회 메소드**/
    @GetMapping("/list")
    public ApiResponse<Page<OrderResponseDto>> getAllOrders(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                            @RequestParam(value = "page", defaultValue = "0") int page,
                                                            @RequestParam(value = "size", defaultValue = "10") int size,
                                                            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
                                                            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc) {
        Page<OrderResponseDto> orders =  orderService.getAllOrders(customUserDetails.getUser(),page,size,sortBy,isAsc);

        return ApiResponse.ok(HttpStatus.OK.value(), "주문 내역 조회에 완성했습니다.",orders);
    }

    /** 주문 상품에 대한 취소 **/
    @PutMapping("/{orderId}/cancel")
    public ApiResponse deleteOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   @PathVariable("orderId") Long orderId) {
        orderService.deleteOrder(customUserDetails.getUser(),orderId);
        return ApiResponse.ok(HttpStatus.OK.value(), "해당 주문이 취소되었습니다.");
    }

    /**배송 완료 상품 반품**/
    @PutMapping("/{orderId}/return")
    public ApiResponse returnOrder(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                   @PathVariable("orderId") Long orderId) {
        orderService.returnOrder(customUserDetails.getUser(),orderId);
        return ApiResponse.ok(HttpStatus.OK.value(), "반품이 완료되었습니다.");

    }

}
