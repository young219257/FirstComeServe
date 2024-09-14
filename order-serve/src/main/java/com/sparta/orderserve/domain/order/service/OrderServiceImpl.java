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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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

        // 제품 정보 Map 생성
        Map<Long, ProductDto> productMap = orderRequestDto.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItemRequestDto::getProductId, item -> Objects.requireNonNull(orderClient.getProductById(item.getProductId()).block()).getData()));

        UserDto userDto= orderClient.getUserById(userId).block().getData();

        /**주문 생성**/
        Order order=Order.of(userDto,productMap,orderRequestDto);
        orderRepository.save(order);

        /**주문 아이템 생성**/
        List<OrderItemRequestDto> orderItems=orderRequestDto.getOrderItems();

        for(OrderItemRequestDto itemRequestDto:orderItems){
            ProductDto product= orderClient.getProductById(itemRequestDto.getProductId()).block().getData();
            OrderItem orderItem=OrderItem.of(order, product.getProductId(), itemRequestDto);
            orderItemRepository.save(orderItem);

        }

        /**배송 정보 생성**/
        Delivery delivery=Delivery.of(order,orderRequestDto);
        deliveryRepository.save(delivery);


    }

    @Override
    @Transactional
    public void completeOrder(Long orderId) throws JsonProcessingException {
        // 주문 조회
        Order order = findOrderById(orderId);

        // 주문 상태를 'ORDER_START'로 변경
        order.updateOrderStatus(OrderStatus.ORDER_START);

       //  주문 아이템 목록 조회
        List<OrderItem> orderItems = order.getOrderItems();

        // OrderItem을 OrderItemRequestDto로 변환
        List<OrderItemRequestDto> orderItemRequestDtos = orderItems.stream()
                .map(OrderItemRequestDto::from)
                .collect(Collectors.toList());

        // Kafka로 재고 업데이트 요청
        orderProducer.completeOrder(orderItemRequestDtos);
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

            return OrderResponseDto.from(order,orderItemResponseDtos);
        });
    }

    @Override
    @Transactional
    public void deleteOrder(Long userId, Long orderId) throws JsonProcessingException {
        Order order= findOrderById(orderId);

        validateOrder(order); //주문 취소 가능 여부 확인
        order.setOrderStatus(OrderStatus.ORDER_CANCEL);

        //주문 상품들에 대한 재고 복구
        List<StockUpdateDto> stockUpdateDtos =new ArrayList<>();

        for(OrderItem orderItem: order.getOrderItems()){

            StockUpdateDto stockUpdateDto = StockUpdateDto.from(orderItem);
            stockUpdateDtos.add(stockUpdateDto);
        }
        orderProducer.deleteOrder(stockUpdateDtos);



    }

    /** 조건 **
     * 배송 완료일 +1 : 주문 불가
     * 배송 완료 주문만 반품 가능
     * **/
    @Override
    @Transactional
    public void returnOrder(Long userId, Long orderId) {

        Order order= findOrderById(orderId);
        //배송 완료일로부터 +1 인 경우
        if(LocalDateTime.now().isAfter(order.getDelivery().getCompletedAt().plusDays(2))){
            throw new InvalidReturnException(ErrorCode.CANNOT_BE_RETURN);
        }

        //반품 신청
        order.signedReturn(order);
        orderRepository.save(order);

    }

    @Override
    public OrderDto getOrderInfo(Long orderId) {
        Order order= findOrderById(orderId);

        return OrderDto.builder().
                totalOrderPrice(order.getTotalPrice()).orderStatus(String.valueOf(order.getOrderStatus())).
                build();

    }


    //주문 가져오는 메소드
    public Order findOrderById(Long orderId) {
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
