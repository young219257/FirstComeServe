package com.sparta.orderserve.domain.order.entity;


import com.sparta.orderserve.domain.delivery.entity.Delivery;
import com.sparta.orderserve.domain.order.dto.OrderItemRequestDto;
import com.sparta.orderserve.domain.order.dto.OrderRequestDto;
import com.sparta.orderserve.domain.order.type.OrderStatus;
import com.sparta.orderserve.domain.product.entity.Product;
import com.sparta.orderserve.domain.user.entity.User;
import com.sparta.orderserve.global.entity.TimeStamped;
import com.sparta.orderserve.global.exception.ErrorCode;
import com.sparta.orderserve.global.exception.NotfoundResourceException;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
public class Order extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String orderer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private long totalPrice;

    @Column
    @Setter
    LocalDateTime returnSignedAt;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "order",cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItems;

    @OneToOne(mappedBy = "order",cascade = CascadeType.REMOVE)
    private Delivery delivery;



    public static Order of(User user, Map<Long,Product> productMap, OrderRequestDto orderRequestDto) {

        int totalPrice=0;

        // 총 가격을 계산
        for (OrderItemRequestDto item : orderRequestDto.getOrderItems()) {
            Product product = productMap.get(item.getProductId());
            if (product != null) {
                totalPrice += product.getPrice() * item.getQuantity();
            } else {
                throw new NotfoundResourceException(ErrorCode.NOTFOUND_PRODUCT);
            }
        }

        return Order.builder().
                user(user).
                totalPrice(totalPrice).
                orderer(user.getUsername()).
                orderStatus(OrderStatus.ORDER_START).
                build();
    }


    public void signedReturn(Order order){
        order.setOrderStatus(OrderStatus.SIGN_RETURN);
        order.setReturnSignedAt(LocalDateTime.now());
    }

}
