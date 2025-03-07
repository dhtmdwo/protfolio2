package com.example.appapi.orders.model;

import com.example.appapi.orders.orderProducts.model.OrderProducts;
import com.example.appapi.payment.model.Payment;
import com.example.appapi.delivery.model.Delivery;
import com.example.appapi.users.model.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private int price;
    private String message;
    private String status;
    private String orderDate; // 주문 날짜

    @OneToMany(mappedBy = "orders")
    private List<OrderProducts> orderProducts;

    @OneToMany(mappedBy = "orders")
    @BatchSize(size = 5)
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private Users user;


    @OneToOne(mappedBy = "orders", fetch = FetchType.LAZY)
    @LazyToOne(LazyToOneOption.NO_PROXY)
    private Delivery delivery;
}