package com.example.appapi.delivery.model;

import lombok.Builder;
import lombok.Getter;

public class DeliveryDto {
    @Builder
    @Getter
    public static class RegisterRequest {
        private String delivery_status;
        private String courier_company;
        private int tracking_number;

        public Delivery toEntity() {
            return Delivery.builder()
                    .delivery_status(delivery_status)
                    .courier_company(courier_company)
                    .tracking_number(tracking_number)
                    .build();
        }
    }
    @Getter
    @Builder
    public static class ListResponse {
        private Long idx;
        private String delivery_status;
        private String courier_company;
        private int tracking_number;

        public static ListResponse from(Delivery delivery) {
            return ListResponse.builder()
                    .idx(delivery.getIdx())
                    .delivery_status(delivery.getDelivery_status())
                    .courier_company(delivery.getCourier_company())
                    .tracking_number(delivery.getTracking_number())
                    .build();

        }
    }

    @Getter
    @Builder
    public static class ReadResponse {
        private Long idx;
        private String delivery_status;
        private String courier_company;
        private int tracking_number;

        public static ReadResponse from(Delivery delivery) {
            return ReadResponse.builder()
                    .idx(delivery.getIdx())
                    .delivery_status(delivery.getDelivery_status())
                    .courier_company(delivery.getCourier_company())
                    .tracking_number(delivery.getTracking_number())
                    .build();
        }
    }
}
