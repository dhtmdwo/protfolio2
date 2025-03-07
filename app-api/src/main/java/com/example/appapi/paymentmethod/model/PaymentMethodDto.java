package com.example.appapi.paymentmethod.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PaymentMethodDto {

    @Getter
    public static class PaymentMethodRegister {
        private String name;
        public PaymentMethod toEntity() {
            return PaymentMethod.builder()
                    .name(name)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentMethodResponse {
        private String name;

        public static PaymentMethodResponse from(PaymentMethod payment) {
            return PaymentMethodResponse.builder()
                    .name(payment.getName())
                    .build();
        }

    }

}
