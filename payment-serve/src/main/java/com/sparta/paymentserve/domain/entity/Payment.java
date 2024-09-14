package com.sparta.paymentserve.domain.entity;


import com.sparta.paymentserve.domain.type.PaymentMethods;
import com.sparta.paymentserve.domain.type.PaymentStatus;
import com.sparta.paymentserve.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class Payment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long orderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentMethods paymentMethod;

    @Column(nullable = false)
    private int orderPrice;

    @Column(nullable = false)
    private int deliveryPrice;

    @Column(nullable = false)
    private int totalPaymentAmount;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime paymentCompletedAt;

    public void updatePaymentStatus(PaymentStatus paymentStatus) {
        this.status = paymentStatus;
    }

    public void approvedPayment() {
        this.paymentCompletedAt=LocalDateTime.now();
        this.status=PaymentStatus.PAYMENT_APPROVE;
    }
}
