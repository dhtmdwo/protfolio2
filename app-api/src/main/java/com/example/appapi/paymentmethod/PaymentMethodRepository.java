package com.example.appapi.paymentmethod;

import com.example.appapi.payment.model.Payment;
import com.example.appapi.paymentmethod.model.PaymentMethod;
import com.example.appapi.paymentmethod.model.PaymentMethodDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
}
