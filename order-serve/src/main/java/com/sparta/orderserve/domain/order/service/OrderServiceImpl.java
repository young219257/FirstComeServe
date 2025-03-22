package com.sparta.orderserve.domain.order.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.orderserve.domain.delivery.entity.Delivery;
import com.sparta.orderserve.domain.delivery.repository.DeliveryRepository;
import com.sparta.orderserve.domain.order.client.OrderClient;
import com.sparta.orderserve.domain.order.dto.*;
import com.sparta.orderserve.domain.order.entity.Order;
import com.sparta.orderserve.domain.order.entity.OrderItem;
import com.sparta.orderserve.domain.order.producer.OrderProducer;
import com.sparta.orderserve.domain.order.repository.OrderItemRepository;
import com.sparta.orderserve.domain.order.repository.OrderRepository;
import com.sparta.orderserve.domain.order.type.OrderStatus;
import com.sparta.orderserve.global.exception.ErrorCode;
import com.sparta.orderserve.global.exception.InvalidOrderStatusException;
import com.sparta.orderserve.global.exception.InvalidReturnException;
import com.sparta.orderserve.global.exception.NotfoundResourceException;
import com.sparta.orderserve.global.exception.handler.dto.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryRepository deliveryRepository;
    private final OrderClient orderClient;
    private final OrderProducer orderProducer;

    @Override
    @Transactional
    public void createOrder(Long userId, OrderRequestDto orderRequestDto) throws JsonProcessingException {
        long startTime = System.currentTimeMillis();

//        // 1. 상품 조회 (외부 API)
//        long productStart = System.currentTimeMillis();
//        Map<Long, ProductDto> productMap = orderRequestDto.getOrderItems().stream()
//                .collect(Collectors.toMap(OrderItemRequestDto::getProductId, item ->
//                        Objects.requireNonNull(orderClient.getProductById(item.getProductId()).block()).getData()));
//        long productEnd = System.currentTimeMillis();
//        System.out.println("Product API Response Time: " + (productEnd - productStart) + "ms");

        // 1. 상품 조회 (외부 API) - 병렬 실행
        long productStart = System.currentTimeMillis();

        List<Mono<ProductDto>> productMonos = orderRequestDto.getOrderItems().stream()
                .map(item -> orderClient.getProductById(item.getProductId()).map(ApiResponse::getData))
                .toList();

        Map<Long, ProductDto> productMap = Flux.mergeSequential(productMonos)  // 순서 유지하며 병렬 실행
                .collectMap(ProductDto::getProductId)
                .block();

        long productEnd = System.currentTimeMillis();
        System.out.println("🚀 Product API 병렬 요청 후 응답 시간: " + (productEnd - productStart) + "ms");



        // 2. 사용자 조회 (외부 API)
        long userStart = System.currentTimeMillis();
        UserDto userDto = orderClient.getUserById(userId).block().getData();
        long userEnd = System.currentTimeMillis();
        System.out.println("User API Response Time: " + (userEnd - userStart) + "ms");

        // 3. 주문 저장 (DB)
        long orderStart = System.currentTimeMillis();
        Order order = Order.of(userDto, productMap, orderRequestDto);
        orderRepository.save(order);
        long orderEnd = System.currentTimeMillis();
        System.out.println("Order DB Save Time: " + (orderEnd - orderStart) + "ms");

        // 4. 주문 아이템 저장 (DB)
        long itemStart = System.currentTimeMillis();
        for (OrderItemRequestDto itemRequestDto : orderRequestDto.getOrderItems()) {
            ProductDto product = orderClient.getProductById(itemRequestDto.getProductId()).block().getData();
            OrderItem orderItem = OrderItem.of(order, product.getProductId(), itemRequestDto);
            orderItemRepository.save(orderItem);
        }
        long itemEnd = System.currentTimeMillis();
        System.out.println("Order Items DB Save Time: " + (itemEnd - itemStart) + "ms");

        // 5. 재고 업데이트 (Kafka)
        long kafkaStart = System.currentTimeMillis();
        orderProducer.completeOrder(orderRequestDto.getOrderItems());
        long kafkaEnd = System.currentTimeMillis();
        System.out.println("Kafka Publish Time: " + (kafkaEnd - kafkaStart) + "ms");

        // 6. 배송 정보 저장 (DB)
        long deliveryStart = System.currentTimeMillis();
        Delivery delivery = Delivery.of(order, orderRequestDto);
        deliveryRepository.save(delivery);
        long deliveryEnd = System.currentTimeMillis();
        System.out.println("Delivery DB Save Time: " + (deliveryEnd - deliveryStart) + "ms");

        long endTime = System.currentTimeMillis();
        System.out.println("Total Execution Time: " + (endTime - startTime) + "ms");
    }

    @Override
    @Transactional
    public Page<OrderResponseDto> getAllOrders(Long userId, int page, int size, String sortBy, boolean isAsc) {
        // 정렬 방향 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        //페이징 처리와 정렬 정보를 포함하는 객체
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Order> orders=orderRepository.findAllByUserId(userId,pageable);
        // OrderResponseDto로 매핑
        return orders.map(order -> {
            // 각 OrderItem에 대해 ProductDto를 가져옴
            List<OrderItemResponseDto> orderItemResponseDtos = order.getOrderItems().stream()
                    .map(orderItem -> {
                        ProductDto productDto = orderClient.getProductById(orderItem.getProductId()).block().getData();
                        return OrderItemResponseDto.of(orderItem, productDto);
                    })
                    .toList();

            return OrderResponseDto.builder()
                    .orderId(order.getId())
                    .orderItems(orderItemResponseDtos)
                    .build();
        });
    }

    @Override
    @Transactional
    public void deleteOrder(Long userId, Long orderId) throws JsonProcessingException {
        Order order=getOrderById(orderId);

        validateOrder(order); //주문 취소 가능 여부 확인
        order.setOrderStatus(OrderStatus.ORDER_CANCEL);

        //주문 상품들에 대한 재고 복구
        List<ProductUpdateRequestDto> productUpdateRequestDtos=new ArrayList<>();

        for(OrderItem orderItem: order.getOrderItems()){

            ProductUpdateRequestDto productUpdateRequestDto=ProductUpdateRequestDto.from(orderItem);
            productUpdateRequestDtos.add(productUpdateRequestDto);
        }
        orderProducer.deleteOrder(productUpdateRequestDtos);



    }

    /** 조건 **
     * 배송 완료일 +1 : 주문 불가
     * 배송 완료 주문만 반품 가능
     * **/
    @Override
    @Transactional
    public void returnOrder(Long userId, Long orderId) {

        Order order=getOrderById(orderId);
        //배송 완료일로부터 +1 인 경우
        if(LocalDateTime.now().isAfter(order.getDelivery().getCompletedAt().plusDays(2))){
            throw new InvalidReturnException(ErrorCode.CANNOT_BE_RETURN);
        }

        //반품 신청
        order.signedReturn(order);
        orderRepository.save(order);

    }


    //주문 가져오는 메소드
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_ORDER));
    }


    //주문 가능한지 확인하는 메소드
    public void validateOrder(Order order) {

        OrderStatus orderStatus=order.getOrderStatus();
        if(orderStatus.equals(OrderStatus.ORDER_CANCEL)){
            throw new InvalidOrderStatusException(ErrorCode.ORDER_ALREADY_CANCEL);
        }
        if(!orderStatus.equals(OrderStatus.ORDER_START)){
            throw new InvalidOrderStatusException(ErrorCode.ORDER_CANNOT_BE_CANCEL);
        }
    }

}
