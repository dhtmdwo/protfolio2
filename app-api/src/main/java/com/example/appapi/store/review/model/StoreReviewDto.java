package com.example.appapi.store.review.model;

import com.example.appapi.store.model.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

public class StoreReviewDto {

    @Getter
    public static class CreateReq {
        @Schema(description = "리뷰 제목", example = "너무 맛있어요")
        private String title;
        @Schema(description = "리뷰 내용", example = "오늘로 세번째 시켜 먹어봐요. 너무 맛있습니다.")
        private String contents;
        @Schema(description = "별점", example = "5")
        private int starPoint;
        private LocalDateTime createdAt;
        @Schema(description = "상점 idx 값", example = "2")
        private Long storeIdx;
        @Schema(description = "이미지 링크", example = "[\"C:\\\\upload\\\\test01.png\", \"C:\\\\upload\\\\test02.png\"]")
        private List<String> imageUrls;

        public StoreReview toEntity(Store store) {
            return StoreReview.builder()
                    .title(title)
                    .contents(contents)
                    .starPoint(starPoint)
                    .createdAt(createdAt)
                    .store(store)
                    .build();
        }
    }

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
        private String userName;

        public static ReviewRes of(StoreReview entity, List<String> imageUrls) {
            return ReviewRes.builder()
                    .idx(entity.getIdx())
                    .title(entity.getTitle())
                    .content(entity.getContents())
                    .starPoint(entity.getStarPoint())
                    .createdAt(entity.getCreatedAt())
                    .imageUrls(imageUrls)
                    .userName(entity.getUsers().getName())
                    .build();
        }
    }

    @Builder
    @Getter
    public static class StoreReivewResponse {
        private Long idx;          // 테이블 idx
        private String title;
        private String contents; // 내용
        private int starPoint; // 별점
        private List<String> reviewImage; // 리뷰 이미지
        private LocalDateTime createdAt; // 작성 날짜
        
        public static StoreReviewDto.StoreReivewResponse from(StoreReview storeReview, List<String> imageUrls) {

            return StoreReviewDto.StoreReivewResponse.builder()
                    .idx(storeReview.getIdx())
                    .title(storeReview.getTitle())
                    .contents(storeReview.getContents())
                    .starPoint(storeReview.getStarPoint())
                    .reviewImage(imageUrls)
                    .createdAt(storeReview.getCreatedAt())
                    .build();
        }
    } // 마이페이지 클라이언트 내가 작성한 식당 리뷰 보기
}
