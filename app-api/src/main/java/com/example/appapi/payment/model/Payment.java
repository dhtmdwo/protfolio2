package com.example.appapi.payment.model;

import com.example.appapi.orders.model.Orders;
import com.example.appapi.paymentmethod.model.PaymentMethod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String paymentId; //
    private String txId;
    private String status;
    private String transactionType;
    private int price; // 금액

    @OneToOne
    @JoinColumn(name = "PaymentMethod_idx")
    private PaymentMethod paymentMethod;

    @ManyToOne
    @JoinColumn(name = "orders_idx")
    private Orders orders;
}
