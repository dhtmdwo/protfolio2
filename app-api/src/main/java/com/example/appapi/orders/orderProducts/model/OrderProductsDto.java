package com.example.appapi.orders.orderProducts.model;

import com.example.appapi.orders.model.Orders;
import com.example.appapi.product.model.Products;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class OrderProductsDto {
    @Builder
    @Getter
    public static class OrderProductResponse {
        @Schema(description = "주문상품 고유번호", example = "1")
        private Long idx;
        @Schema(description = "주문상품 갯수", example = "4")
        private int quantity;
        private ProductsResponse productsResponse;

        public static OrderProductResponse of(OrderProducts orderProducts, OrderProductsDto.ProductsResponse products) {
            return OrderProductResponse.builder()
                    .idx(orderProducts.getIdx())
                    .quantity(orderProducts.getQuantity())
                    .productsResponse(products)
                    .build();
        }
    }
    @Builder
    @Getter
    public static class ProductsResponse {
        @Schema(description = "상품명", example = "김치찌개 밀키트")
        private String name;
        @Schema(description = "상품 가격(원 단위)", example = "9900")
        private int price;
        public static ProductsResponse of(Products products) {
            return ProductsResponse.builder()
                    .name(products.getName())
                    .price(products.getPrice())
                    .build();
        }
    }
    @Builder
    @Getter
    public static class OrderRegisterRequest {  // 등록
        private List<OrderProductRegisterRequest> orderProductRegisterRequest;
        @Schema(description = "주문 상품 총 가격", example = "150000")
        private int totalPrice;
    }
    @Builder
    @Getter
    public static class OrderRegisterResponse {
        @Schema(description = "주문서 고유 식별자", example = "1")
        private Long orderIdx;
        @Schema(description = "주문서 총 금액", example = "150000")
        private int totalPrice;

        public static OrderRegisterResponse from(Orders savedOrder, int totalPrice) {
            return OrderRegisterResponse.builder()
                    .orderIdx(savedOrder.getIdx())
                    .totalPrice(totalPrice)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class OrderProductRegisterRequest {
        @Schema(description = "주문 상품 고유번호", example = "1")
        private Long productIdx;
        @Schema(description = "주문 상품 갯수", example = "4")
        private int quantity;
        public OrderProducts toEntity(Orders order, Products product) {
            return OrderProducts.builder()
                    .orders(order)
                    .quantity(quantity)
                    .products(product)
                    .reviewStatus("Not Reviewed")
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ListProductsResponse {
        @Schema(description = "주문 상품 고유번호", example = "1")
        private Long idx;
        @Schema(description = "주문 상품 총 갯수", example = "4")
        private int quantity;
        private OrderProductsDto.ListProductResponse ListProductResponse;
        public static ListProductsResponse from (OrderProducts orderProducts, OrderProductsDto.ListProductResponse products) {
            return ListProductsResponse.builder()
                    .idx(orderProducts.getIdx())
                    .quantity(orderProducts.getQuantity())
                    .ListProductResponse(products)
                    .build();
        }
    }
    @Builder
    @Getter
    public static class ListProductResponse{
        @Schema(description = "상품 고유 번호", example = "1")
        private Long idx;
        @Schema(description = "상품명", example = "김치찌개 밀키트")
        private String name;
        @Schema(description = "상품 가격", example = "10000")
        private int price;
        public static ListProductResponse from(Products product){
            return ListProductResponse.builder()
                    .idx(product.getIdx())
                    .name(product.getName())
                    .price(product.getPrice())
                    .build();

        }
    }


}
