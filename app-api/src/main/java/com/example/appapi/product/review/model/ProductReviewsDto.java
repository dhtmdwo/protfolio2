package com.example.appapi.product.review.model;

import com.example.appapi.orders.orderProducts.model.OrderProducts;
import com.example.appapi.product.model.Products;
import com.example.appapi.product.review.images.model.ProductReviewImages;
import com.example.appapi.users.model.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ProductReviewsDto {

    @Getter
    @Builder
    public static class InfoResponse {
        @Schema(description = "리뷰의 별점", example = "5")
        private int starPoint;

        @Schema(description = "리뷰 내용", example = "이 제품은 정말 훌륭합니다!")
        private String content;

        @Schema(description = "리뷰 작성 날짜 및 시간", example = "2025-03-06T09:26:58")
        private LocalDateTime createdAt;

        @Schema(description = "리뷰 이미지 URL 목록")
        private List<String> imageUrls;

        public static InfoResponse fromEntity(ProductReviews reviews) {
            return InfoResponse.builder()
                    .starPoint(reviews.getStarPoint())
                    .content(reviews.getContent())
                    .createdAt(reviews.getCreatedAt())
                    .imageUrls(reviews.getImages().stream()
                            .map(ProductReviewImages::getImagePath)
                            .collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ProductReviewResponse {
        @Schema(description = "리뷰 id값", example = "1")
        private Long idx;

        @Schema(description = "리뷰 제목", example = "이 제품은 정말 훌륭합니다!")
        private String title;
        
        @Schema(description = "리뷰 내용", example = "이 제품은 정말 훌륭합니다!")
        private String contents;

        @Schema(description = "리뷰의 별점", example = "5")
        private int starPoint;

        @Schema(description = "리뷰 이미지 URL 목록")
        private List<String> reviewImage;

        @Schema(description = "리뷰 작성 날짜 및 시간", example = "2025-03-06T09:26:58")
        private LocalDateTime createdAt;

        public static ProductReviewResponse from(ProductReviews productReviews, List<String> imageUrls) {
            return ProductReviewResponse.builder()
                    .idx(productReviews.getIdx())
                    .title(productReviews.getTitle())
                    .contents(productReviews.getContent())
                    .starPoint(productReviews.getStarPoint())
                    .reviewImage(imageUrls)
                    .createdAt(productReviews.getCreatedAt())
                    .build();
        }
    }

    @Getter
    public static class CreateReq {
        @Schema(description = "리뷰 제목", example = "나쁘지 않아요")
        private String title;
        @Schema(description = "리뷰 내용", example = "쏘쏘합니다")
        private String content;
        @Schema(description = "별점", example = "4")
        private int starPoint;
        @Schema(description = "상품 idx 값", example = "2")
        private Long productIdx;
        @Schema(description = "이미지 링크", example = "[\"C:\\\\upload\\\\test01.png\", \"C:\\\\upload\\\\test02.png\"]")
        private List<String> imageUrls;
        @Schema(description = "상품 주문 테이블 idx 값", example = "1")
        private Long orderProductIdx;

        private LocalDateTime createdAt;

        public ProductReviews toEntity(Products products, Users user, OrderProducts orderProducts) {
            return ProductReviews.builder()
                    .title(title)
                    .content(content)
                    .starPoint(starPoint)
                    .createdAt(createdAt)
                    .products(products)
                    .user(user)
                    .orderProducts(orderProducts)
                    .build();
        }
    } // 리뷰 작성용 dto

    @Getter
    @Builder
    @Setter
    public static class ReviewRes {
        private Long idx;
        private String title;
        private String content;
        private int starPoint;
        private LocalDateTime createdAt;
        private List<String> imageUrls;
        private Long productIdx;

        public static ProductReviewsDto.ReviewRes of(ProductReviews entity, List<String> imageUrls) {
            return ProductReviewsDto.ReviewRes.builder()
                    .idx(entity.getIdx())
                    .title(entity.getTitle())
                    .content(entity.getContent())
                    .starPoint(entity.getStarPoint())
                    .createdAt(entity.getCreatedAt())
                    .imageUrls(imageUrls)
                    .productIdx(entity.getIdx())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class RegisterRequest {
        @Schema(description = "상품의 고유 식별자", example = "1")
        private Long productIdx;

        @Schema(description = "리뷰 제목", example = "최고의 제품!")
        private String title;

        @Schema(description = "리뷰 내용", example = "이 제품은 제 기대를 완전히 충족시켰습니다.")
        private String content;

        @Schema(description = "리뷰 이미지 URL", example = "/images/review1.jpg")
        private String image;

        @Schema(description = "리뷰의 별점", example = "5")
        private int starPoint;

        public ProductReviews toEntity(Users users) {
            return ProductReviews.builder()
                    .user(users)
                    .products(Products.builder()
                            .idx(productIdx)
                            .build())
                    .content(content)
                    .title(title)
                    .starPoint(starPoint)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class ReviewablesResponse {
        private Long orderProductIdx;
        private String name;
        private String imageUrl;
        public static ReviewablesResponse from(String name, String imageUrl, Long idx) {
            return ReviewablesResponse.builder()
                    .orderProductIdx(idx)
                    .name(name)
                    .imageUrl(imageUrl)
                    .build();
        }
    }
}

