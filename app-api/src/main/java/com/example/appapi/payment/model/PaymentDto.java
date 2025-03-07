package com.example.appapi.payment.model;

import com.example.appapi.orders.model.Orders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

public class PaymentDto {
    @Getter
    public static class PaymentRegister {
        private Long orderIdx;
        private String paymentId;
        private String txId;
        private String transactionType;
        public Payment toEntity(Long orderIdx) {
            return Payment.builder()
                    .orders(Orders.builder()
                            .idx(orderIdx)
                            .build())
                    .paymentId(paymentId)
                    .transactionType(transactionType)
                    .txId(txId)
                    .build();
        }

    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentResponse {
        private String status;
        private String paymentId;

        public static PaymentResponse from(Payment payment) {
            return PaymentResponse.builder()
                    .paymentId(payment.getPaymentId())
                    .status(payment.getStatus())
                    .build();
        }

    }

}
