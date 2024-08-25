package com.sparta.firstseversystem.domain.order.service;

import com.sparta.firstseversystem.domain.delivery.entity.Delivery;
import com.sparta.firstseversystem.domain.delivery.repository.DeliveryRepository;
import com.sparta.firstseversystem.domain.delivery.type.DeliveryStatus;
import com.sparta.firstseversystem.domain.order.dto.OrderRequestDto;
import com.sparta.firstseversystem.domain.order.dto.OrderResponseDto;
import com.sparta.firstseversystem.domain.order.entity.Order;
import com.sparta.firstseversystem.domain.order.entity.OrderItem;
import com.sparta.firstseversystem.domain.order.repository.OrderItemRepository;
import com.sparta.firstseversystem.domain.order.repository.OrderRepository;
import com.sparta.firstseversystem.domain.order.type.OrderStatus;
import com.sparta.firstseversystem.domain.product.entity.Product;
import com.sparta.firstseversystem.domain.product.repository.ProductRepository;
import com.sparta.firstseversystem.domain.user.entity.User;
import com.sparta.firstseversystem.global.entity.TimeStamped;
import com.sparta.firstseversystem.global.exception.ErrorCode;
import com.sparta.firstseversystem.global.exception.InvalidOrderStatusException;
import com.sparta.firstseversystem.global.exception.InvalidReturnException;
import com.sparta.firstseversystem.global.exception.NotfoundResourceException;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;

    @Override
    public void createOrder(User user, OrderRequestDto orderRequestDto) {

        Product product=productRepository.findById(orderRequestDto.getProductId()).orElseThrow(()-> new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT));

        /**주문 생성**/
        Order order=Order.builder().
                user(user).
                totalPrice(product.getPrice() * orderRequestDto.getQuantity()).
                orderer(user.getUsername()).
                orderStatus(OrderStatus.ORDER_START).
                build();
        orderRepository.save(order);

        /**주문 아이템 생성**/
        OrderItem orderItem=OrderItem.builder().
                order(order).
                product(product).
                quantity(orderRequestDto.getQuantity()).
                build();
        orderItemRepository.save(orderItem);


        /**배송 정보 생성**/
        Delivery delivery=Delivery.builder().
                order(order).
                address(orderRequestDto.getAddress()).
                phoneNumber(orderRequestDto.getPhoneNumber()).
                deliveryStatus(DeliveryStatus.READY_DELIVERY).
                build();
        deliveryRepository.save(delivery);

        //주문 후 수량 변경 : 현재 수량 - orderRequestDto.getQuantity()
        product.setStockQuantity(product.getStockQuantity()- orderRequestDto.getQuantity());
        productRepository.save(product);

    }

    @Override
    @Transactional
    public Page<OrderResponseDto> getAllOrders(User user, int page, int size, String sortBy, boolean isAsc) {
        // 정렬 방향 설정
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        //페이징 처리와 정렬 정보를 포함하는 객체
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<Order> orders=orderRepository.findAllByUser(user,pageable);

        return orders.map(OrderResponseDto::from);
    }

    @Override
    @Transactional
    public void deleteOrder(User user, Long orderId) {
        Order order=getOrderById(orderId);

        validateOrder(order); //주문 가능 여부 확인
        order.setOrderStatus(OrderStatus.ORDER_CANCEL);

        //주문 상품들에 대한 재고 복구
        List<OrderItem> orderItems= order.getOrderItems();
        for(OrderItem orderItem:orderItems){
            orderItem.getProduct().setStockQuantity(orderItem.getProduct().getStockQuantity() - orderItem.getQuantity());
            orderItemRepository.save(orderItem);
        }

    }

    /** 조건 **
     * 배송 완료일 +1 : 주문 불가
     * 배송 완료 주문만 반품 가능
     * **/
    @Override
    @Transactional
    public void returnOrder(User user, Long orderId) {

        Order order=getOrderById(orderId);
        //배송 완료일로부터 +1 인 경우
        if(LocalDateTime.now().isAfter(order.getDelivery().getCompletedAt().plusDays(2))){
            throw new InvalidReturnException(ErrorCode.CANNOT_BE_RETURN);
        }
        order.setOrderStatus(OrderStatus.SIGN_RETURN);
        order.setReturnSignedAt(LocalDateTime.now());
        orderRepository.save(order);

    }


    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_ORDER));
    }

    /**주문 가능한지 확인하는 메소드**/
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
