package com.example.appapi.orders.model;

import com.example.appapi.delivery.model.Delivery;
import com.example.appapi.orders.orderProducts.model.OrderProductsDto;
import com.example.appapi.payment.model.Payment;
import com.example.appapi.users.model.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class OrdersDto {
    @Builder
    @Getter
    public static class OrdersRegister {
        @Schema(description = "주문 요청사항", example = "유통기한 긴 제품으로 보내주세요")
        private String message;
    }
    @Builder
    @Getter
    public static class RegisterRequest {
        private String message;
        private String status;

        public Orders toEntity(int totalPrice) {
            return Orders.builder()
                    .price(totalPrice)
                    .message(message)
                    .status("Pending")
                    .build();
        }
    }


    @Getter
    @Builder
    public static class ListResponse {
        private Long idx;
        private int price;
        private String message;
        private String status;

        public static ListResponse from(Orders orders) {
            return ListResponse.builder()
                    .idx(orders.getIdx())
                    .price(orders.getPrice())
                    .message(orders.getMessage())
                    .status(orders.getStatus())
                    .build();
        }
    }
    @Getter
    @Builder
    public static class ReadResponse {
        private Long idx;
        private int price;
        private String message;
        private String status;

        public static ReadResponse from(Orders orders) {
            return ReadResponse.builder()
                    .idx(orders.getIdx())
                    .price(orders.getPrice())
                    .message(orders.getMessage())
                    .status(orders.getStatus())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class OrdersResponse {
        @Schema(description = "주문서 고유번호", example = "1")
        private Long idx;
        @Schema(description = "주문 총 금액(원 단위)", example = "150000")
        private int price;
        @Schema(description = "주문 요청사항", example = "유통기한 긴 제품으로 보내주세요")
        private String message;
        @Schema(description = "주문상태", example = "Paying")
        private String status;

        private List<OrderProductsDto.OrderProductResponse> OrderProductResponse;

        public static OrdersResponse from(Orders orders, List<OrderProductsDto.OrderProductResponse> resp) {
            return OrdersResponse.builder()
                    .idx(orders.getIdx())
                    .price(orders.getPrice())
                    .message(orders.getMessage())
                    .status(orders.getStatus())
                    .OrderProductResponse(resp)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class OrderMypageList {
        private Long idx;
        private String orderDate;
        private String status;
        private List<MyOrder> myOrderList;

        public static OrderMypageList from(Orders orders, List<MyOrder> myOrderList) {
            return OrderMypageList.builder()
                    .idx(orders.getIdx()) // 주문 번호
                    .orderDate(orders.getOrderDate()) // 주문 날짜
                    .status(orders.getStatus()) // 주문 상태
                    .myOrderList(myOrderList)
                    .build();
        }
    } // 마이페이지 클라이언트 주문 목록

    @Getter
    @Builder
    public static class MyOrder {
        private String productImage;
        private String name;
        private int quantity;

        public static MyOrder from(String productName, int quantity, String productImage) {
            return MyOrder.builder()
                    .name(productName) // 주문 상태
                    .quantity(quantity)
                    .productImage(productImage)
                    .build();
        }
    } // 마이페이지 클라이언트 주문 리스트 => id, 상품 이름, 상품 개수

    @Getter
    @Builder
    public static class OrderMypageDetails {
//        private Long idx; // 주문 번호
//        private String orderDate; // 주문 날짜
//        private String status; // 주문 상태
//
//        private List<MyOrder> myOrderList; // 주문 리스트
        
        private OrderMypageList orderMypageList; // 위에 네개 합쳐짐

        private int orderPrice; // 주문 금액
        private int paymentPrice; // 결제 금액
        private String paymentMethod; // 결제 방법

        private String name; // 유저 이름
        private String phone; // 유저 전화번호
        private String address; // 유저 주소 + 상세주소
        private String message; // 주문 요청사항

        private Long paymentIdx; // 주문 취소시 사용
        private String courier_company; // 택배 회사
        private int tracking_number;    // 운송장 번호

        public static OrderMypageDetails from(OrderMypageList orderMypageList, Orders orders, Users users, Payment payment, Delivery delivery) {
            String paymentMethod = payment.getPaymentMethod().getName();

            return OrderMypageDetails.builder()
                    .orderMypageList(orderMypageList) // 네개 합침
                    .orderPrice(orders.getPrice()) // 주문 금액
                    .paymentPrice(payment.getPrice()) // 결제 금액
                    .paymentMethod(paymentMethod) // 결제 방법
                    .name(users.getName()) // 유저 이름
                    .phone(users.getPhone()) // 유저 전화번호
                    .address(users.getAddress() + " "+ users.getAddressDetail()) // 유저 주소 + 상세주소
                    .message(orders.getMessage()) //
                    .paymentIdx(payment.getIdx())
                    .courier_company(delivery.getCourier_company())
                    .tracking_number(delivery.getTracking_number())
                    .build();
        }
    } // 마이페이지 클라이언트 주문 목록

    @Getter
    @Builder
    public static class OrdersRegisterResponse {
        @Schema(description = "주문서 고유번호", example = "1")
        private Long idx;
        @Schema(description = "주문 요청사항", example = "유통기한 긴 제품으로 보내주세요")
        private String message;

        public static OrdersRegisterResponse from(Orders order) {
            return OrdersRegisterResponse.builder()
                    .idx(order.getIdx())
                    .message(order.getMessage())
                    .build();
        }
    }
}
