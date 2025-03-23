package com.example.appapi.product.model;

import com.example.appapi.product.review.model.ProductReviews;
import com.example.appapi.store.model.Store;
import com.example.appapi.store.model.StoreDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProductsDto {

    @Getter
    @Builder
    public static class Create {
        @Schema(description = "상품의 이름", example = "스마트폰")
        private String name;

        @Schema(description = "상품의 설명", example = "최신 모델의 고급 기능을 갖춘 스마트폰")
        private String description;

        @Schema(description = "상품의 가격 (원 단위)", example = "999000")
        private int price;

        @Schema(description = "재고 수량", example = "50")
        private int stock;

        @Schema(description = "상품 이미지 경로들의 리스트")
        private List<String> imagePaths;

        public Products toEntity() {
            return Products.builder()
                    .name(name)
                    .description(description)
                    .price(price)
                    .stock(stock)
                    .build();
        }
    }

    @Getter
    @Builder
    public static class InfoResponse {
        @Schema(description = "상품의 고유 식별자", example = "1")
        private Long idx;

        @Schema(description = "상품의 이름", example = "스마트폰")
        private String name;

        @Schema(description = "대표 이미지 경로", example = "/images/product1.jpg")
        private String imgPath;

        @Schema(description = "평균 별점", example = "4.5")
        private double starPoint;

        @Schema(description = "리뷰 수", example = "150")
        private int reviewCnt;

        @Schema(description = "상품의 가격 (원 단위)", example = "999000")
        private int price;

        public static InfoResponse fromEntity(Products products) {
            return InfoResponse.builder()
                    .idx(products.getIdx())
                    .name(products.getName())
                    .imgPath(products.getImages().get(0).getImagePath())
                    .price(products.getPrice())
                    //반 정규화 적용 전
//                    .reviewCnt(products.getReviewCount())
//                    .starPoint(products.getStarPoint())
                    //N + 1 문제 발생
                    .reviewCnt(products.getReviews().size())
                    .starPoint(products.getReviews().stream()
                            .mapToInt(ProductReviews::getStarPoint)
                            .average()
                            .orElse(0.0))
                    .build();
        }
    }

    @Getter
    @Builder
    public static class DetailResponse {
        @Schema(description = "상품의 고유 식별자", example = "1")
        private Long idx;

        @Schema(description = "대표 이미지 경로", example = "/images/product1.jpg")
        private String imgPath;

        @Schema(description = "평균 별점", example = "4.5")
        private double starPoint;

        @Schema(description = "리뷰 수", example = "150")
        private int reviewCnt;

        @Schema(description = "상품의 이름", example = "스마트폰")
        private String name;

        @Schema(description = "상품의 가격 (원 단위)", example = "999000")
        private int price;

        @Schema(description = "상품의 상세 설명", example = "이 스마트폰은 6.5인치 디스플레이를 특징으로 합니다...")
        private String description;

        public static DetailResponse fromEntity(Products products) {
            return DetailResponse.builder()
                    .idx(products.getIdx())
                    .name(products.getName())
                    .imgPath(products.getImages().get(0).getImagePath())
                    .price(products.getPrice())
                    //반정규화 전
                    .reviewCnt(products.getReviews().size())
                    .starPoint(products.getReviews().stream()
                            .mapToInt(ProductReviews::getStarPoint)
                            .average()
                            .orElse(0.0))
                    //반정규화 전
                    .description(products.getDescription())
                    .build();
        }
    }

    @Getter
    @Builder
    public static class ProductRes {
        @Schema(description = "상품의 고유 식별자", example = "1")
        private Long idx;

        @Schema(description = "상품의 이름", example = "스마트폰")
        private String name;

        @Schema(description = "상품의 가격 (원 단위)", example = "999000")
        private int price;

        @Setter
        @Schema(description = "상품 이미지 URL들의 리스트")
        private List<String> imageUrls;

        public static ProductRes fromEntity(Products entity) {
            return ProductRes.builder()
                    .idx(entity.getIdx())
                    .name(entity.getName())
                    .price(entity.getPrice())
                    .imageUrls(
                            entity.getImages() == null ? List.of() : entity.getImages().stream()
                                    .map(image -> image.getImagePath()).toList()
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductPageResponseDto<T> {  // DTO 제네릭화
        @Schema(description = "현재 페이지 번호", example = "0")
        private int page;
        @Schema(description = "한 페이지 당 데이터 개수", example = "10")
        private int size;
        @Schema(description = "총 데이터 개수", example = "13")
        private long totalElements;
        @Schema(description = "총 페이지 개수", example = "2")
        private int totalPages;
        @Schema(description = "다음 페이지 여부", example = "true")
        private boolean hasNext;
        @Schema(description = "이전 페이지 여부", example = "false")
        private boolean hasPrevious;

        private List<T> products; // DTO 타입을 제네릭으로 변환

        public static <T> ProductPageResponseDto<T> from(Page<Products> productPage, Function<Products, T> dtoConverter) {
            return ProductPageResponseDto.<T>builder()
                    .page(productPage.getNumber())
                    .size(productPage.getSize())
                    .totalElements(productPage.getTotalElements())
                    .totalPages(productPage.getTotalPages())
                    .hasNext(productPage.hasNext())
                    .hasPrevious(productPage.hasPrevious())
                    .products(productPage.stream().map(dtoConverter).collect(Collectors.toList()))
                    .build();
        }
    }
}