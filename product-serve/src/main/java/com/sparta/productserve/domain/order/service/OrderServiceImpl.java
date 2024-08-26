package com.sparta.productserve.domain.order.service;

import com.sparta.productserve.domain.delivery.entity.Delivery;
import com.sparta.productserve.domain.delivery.repository.DeliveryRepository;
import com.sparta.productserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.productserve.domain.order.dto.OrderRequestDto;
import com.sparta.productserve.domain.order.dto.OrderResponseDto;
import com.sparta.productserve.domain.order.entity.Order;
import com.sparta.productserve.domain.order.entity.OrderItem;
import com.sparta.productserve.domain.order.repository.OrderItemRepository;
import com.sparta.productserve.domain.order.repository.OrderRepository;
import com.sparta.productserve.domain.order.type.OrderStatus;
import com.sparta.productserve.domain.product.entity.Product;
import com.sparta.productserve.domain.product.repository.ProductRepository;
import com.sparta.productserve.domain.user.entity.User;
import com.sparta.productserve.global.exception.ErrorCode;
import com.sparta.productserve.global.exception.InvalidOrderStatusException;
import com.sparta.productserve.global.exception.InvalidReturnException;
import com.sparta.productserve.global.exception.NotfoundResourceException;
import com.sparta.productserve.domain.delivery.entity.Delivery;
import com.sparta.productserve.domain.delivery.repository.DeliveryRepository;
import com.sparta.productserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.productserve.domain.order.entity.Order;
import com.sparta.productserve.domain.order.entity.OrderItem;
import com.sparta.productserve.domain.order.repository.OrderItemRepository;
import com.sparta.productserve.domain.order.repository.OrderRepository;
import com.sparta.productserve.domain.order.type.OrderStatus;
import com.sparta.productserve.domain.product.entity.Product;
import com.sparta.productserve.domain.product.repository.ProductRepository;
import com.sparta.productserve.global.exception.ErrorCode;
import com.sparta.productserve.global.exception.InvalidOrderStatusException;
import com.sparta.productserve.global.exception.InvalidReturnException;
import com.sparta.productserve.global.exception.NotfoundResourceException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final DeliveryRepository deliveryRepository;

    @Override
    @Transactional
    public void createOrder(User user, OrderRequestDto orderRequestDto) {


        // 제품 정보 Map 생성
        Map<Long, Product> productMap = orderRequestDto.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItemRequestDto::getProductId, item -> getProductById(item.getProductId())));


        /**주문 생성**/
        Order order=Order.of(user,productMap,orderRequestDto);
        orderRepository.save(order);

        /**주문 아이템 생성**/
        for(OrderItemRequestDto itemRequestDto:orderRequestDto.getOrderItems()){
            Product product=getProductById(itemRequestDto.getProductId());

            OrderItem orderItem=OrderItem.of(order,product,itemRequestDto);
            orderItemRepository.save(orderItem);

            //주문 수량 변경 : 현재 수량 - orderRequestDto.getQuantity()
            product.setStockQuantity(product.getStockQuantity()-orderItem.getQuantity());
            productRepository.save(product);
        }

        /**배송 정보 생성**/
        Delivery delivery=Delivery.of(order,orderRequestDto);
        deliveryRepository.save(delivery);

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

        validateOrder(order); //주문 취소 가능 여부 확인
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

        //반품 신청
        order.signedReturn(order);
        orderRepository.save(order);

    }


    //주문 가져오는 메소드
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow(()->new NotfoundResourceException(ErrorCode.NOTFOUND_ORDER));
    }

    //상품 가져오는 메소드
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(()-> new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT));

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
