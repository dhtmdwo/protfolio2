package com.example.appapi.store.model;

import com.example.appapi.category.model.Category;
import com.example.appapi.likes.model.Likes;
import com.example.appapi.users.model.Users;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StoreDto {

    // store 생성 request
    @Getter
    public static class CreateStoreRequestDto {
        @Schema(description = "식당 이름", example = "모스키친")
        private String name;
        @Schema(description = "식당 설명", example = "신선한 재료와 정성을 담아 맛있는 한 끼를 제공하는 맛집입니다.")
        private String description;
        @Schema(description = "식당 전화번호", example = "02-111-2222")
        private String callNumber;
        @Schema(description = "예약 시작 시간", example = "17:00")
        private LocalTime startTime;
        @Schema(description = "예약 종료 시간", example = "21:00")
        private LocalTime endTime;
        @Schema(description = "식당 운영 시간", example = "10:00~22:00")
        private String openingHours;
        @Schema(description = "식당 주소", example = "서울시 동작구 보라매로 87")
        private String address;
        @Schema(description = "식당 간략 주소", example = "서울 동작구")
        private String shortAddress;
        @Schema(description = "카테고리 번호", example = "12")
        private Long categoryIdx;

        List<ClosedDayRequestDto> closedDayList = new ArrayList<>();
        private List<String> imagePaths;

        public Store toEntity(Users user, Category category) {
            return Store.builder()
                    .name(name)
                    .description(description)
                    .callNumber(callNumber)
                    .startTime(startTime)
                    .endTime(endTime)
                    .openingHours(openingHours)
                    .address(address)
                    .shortAddress(shortAddress)
                    .user(user)
                    .allowed(AllowedStatus.WAITING)
                    .category(category)
                    .likesCount(0L)
                    .build();
        }
    }

    // 휴무일 request
    @Getter
    public static class ClosedDayRequestDto {
        @Schema(description = "휴무일", example = "MON")
        private String day;

        public StoreClosedDay toEntity(Store store) {
            return StoreClosedDay.builder()
                    .day(day)
                    .store(store)
                    .build();
        }
    }

    // 휴무일 response
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClosedDayResponseDto {
        @Schema(description = "식당 휴무일 고유번호", example = "1")
        private Long idx;
        @Schema(description = "식당 휴무일", example = "MON")
        private String day;

        public static ClosedDayResponseDto from(StoreClosedDay storeClosedDay) {
            return ClosedDayResponseDto.builder()
                    .idx(storeClosedDay.getIdx())
                    .day(storeClosedDay.getDay())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreResponseDto {
        @Schema(description = "식당 고유 번호", example = "1")
        private Long idx;
        @Schema(description = "식당 이름", example = "모스키친")
        private String name;
        @Schema(description = "식당 설명", example = "신선한 재료와 정성을 담아 맛있는 한 끼를 제공하는 맛집입니다.")
        private String description;
        @Schema(description = "식당 전화번호", example = "02-111-2222")
        private String callNumber;
        @Schema(description = "예약 시작 시간", example = "17:00")
        private LocalTime startTime;
        @Schema(description = "예약 종료 고유번호", example = "21:00")
        private LocalTime endTime;
        @Schema(description = "식당 운영 시간", example = "10:00~22:00")
        private String openingHours;
        @Schema(description = "식당 주소", example = "서울시 동작구 보라매로 87")
        private String address;
        @Schema(description = "식당 간략 주소", example = "서울 동작구")
        private String shortAddress;
        @Schema(description = "식당 등록 상태", example = "YES")
        private AllowedStatus allowed;
        @Schema(description = "카테고리 이름", example = "돈까스")
        private String categoryName;
        @Schema(description = "카테고리 idx", example = "1")
        private Long categoryIdx;
        @Schema(description = "총 리뷰 수", example = "52")
        private int reviewCnt;
        @Schema(description = "평균 별점", example = "4.5")
        private double starPoint;

        @Setter
        @Schema(description = "식당 휴무일 목록")
        List<ClosedDayResponseDto> closedDayList = new ArrayList<>();
        @Setter
        @Schema(description = "식당 이미지 목록")
        private List<String> imagePaths;

        List<Likes> likesList;

        public static StoreResponseDto from(Store store) {
            return StoreResponseDto.builder()
                    .idx(store.getIdx())
                    .name(store.getName())
                    .description(store.getDescription())
                    .callNumber(store.getCallNumber())
                    .startTime(store.getStartTime())
                    .endTime(store.getEndTime())
                    .openingHours(store.getOpeningHours())
                    .address(store.getAddress())
                    .shortAddress(store.getShortAddress())
                    .allowed(store.getAllowed())
                    .categoryName(store.getCategory().getName())
                    .categoryIdx(store.getCategory().getIdx())
                    .starPoint(store.getStarPoint())
                    .reviewCnt(store.getReviewCnt())
                    .closedDayList(store.getClosedDayList() == null ? null
                            : store.getClosedDayList().stream().map(StoreDto.ClosedDayResponseDto::from).collect(Collectors.toList()))
                    .imagePaths(
                            store.getImages() == null ? List.of() : store.getImages().stream()
                                    .map(image -> image.getImagePath()).toList()
                    )
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StoreSimpleResponseDto {
        @Schema(description = "식당 고유 번호", example = "1")
        private Long idx;
        @Schema(description = "식당 이름", example = "모스키친")
        private String name;
        @Schema(description = "식당 간략 주소", example = "서울 동작구")
        private String shortAddress;
        @Schema(description = "카테고리 이름", example = "돈까스")
        private String categoryName;
        @Schema(description = "식당 대표 이미지", example = "thumbnail.png")
        private String thumbnail;
        @Schema(description = "총 리뷰 수", example = "52")
        private int reviewCnt;
        @Schema(description = "평균 별점", example = "4.5")
        private double starPoint;

        public static StoreSimpleResponseDto from(Store store) {
            return StoreSimpleResponseDto.builder()
                    .idx(store.getIdx())
                    .name(store.getName())
                    .shortAddress(store.getShortAddress())
                    .categoryName(store.getCategory().getName())
                    .starPoint(store.getStarPoint())
                    .reviewCnt(store.getReviewCnt())
                    .thumbnail(store.getImages().isEmpty() ? null : store.getImages().get(0).getImagePath())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StorePageResponseDto<T> {  // DTO 제네릭화
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

        private List<T> stores; // DTO 타입을 제네릭으로 변환

        public static <T> StorePageResponseDto<T> from(Page<Store> storePage, Function<Store, T> dtoConverter) {
            return StorePageResponseDto.<T>builder()
                    .page(storePage.getNumber())
                    .size(storePage.getSize())
                    .totalElements(storePage.getTotalElements())
                    .totalPages(storePage.getTotalPages())
                    .hasNext(storePage.hasNext())
                    .hasPrevious(storePage.hasPrevious())
                    .stores(storePage.stream().map(dtoConverter).collect(Collectors.toList()))
                    .build();
        }
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AdminStoreResponse {
        @Schema(description = "식당 고유 번호", example = "1")
        private Long idx;
        @Schema(description = "식당 이름", example = "모스키친")
        private String name;
        @Schema(description = "식당 간략 주소", example = "서울 동작구")
        private String shortAddress;
        @Schema(description = "카테고리 이름", example = "돈까스")
        private String categoryName;
        @Schema(description = "식당 대표 이미지", example = "thumbnail.png")
        private String thumbnail;
        @Schema(description = "식당 등록 상태", example = "WAITING")
        private AllowedStatus allowed;

        public static AdminStoreResponse from(Store store) {
            return AdminStoreResponse.builder()
                    .idx(store.getIdx())
                    .name(store.getName())
                    .shortAddress(store.getShortAddress())
                    .categoryName(store.getCategory().getName())
                    .thumbnail(store.getImages().isEmpty() ? null : store.getImages().get(0).getImagePath())
                    .allowed(store.getAllowed())
                    .build();
        }
    }



    @Getter
    public static class UpdateStoreStatusDto {
        @Schema(description = "카테고리 번호", example = "1")
        private Long categoryIdx;
        @Schema(description = "식당 상태", example = "YES")
        private AllowedStatus allowed;
    }

    @Getter
    @Builder
    public static class MyStoreResponseDto {
        @Schema(description = "식당 고유 번호", example = "1")
        private Long idx;
        @Schema(description = "식당 리뷰 별점", example = "4.8")
        private int starPoint;
        @Schema(description = "식당 이름", example = "모스키친")
        private String name;
        @Schema(description = "식당 설명", example = "신선한 재료와 정성을 담아 맛있는 한 끼를 제공하는 맛집입니다.")
        private String description;
        @Schema(description = "이미지 주소", example = "C:/Users/YourName/Pictures/moskitchen_logo.jpg")
        private String imagePath;
        @Schema(description = "식당 전화번호", example = "02-111-2222")
        private String callNumber;
        @Schema(description = "식당 운영 시간", example = "10:00~22:00")
        private String openingHours;
        @Schema(description = "예약 시작 시간", example = "10:00")
        private LocalTime startTime;
        @Schema(description = "예약 종료 고유번호", example = "14:00")
        private LocalTime endTime;
        @Schema(description = "식당 주소", example = "서울시 동작구 보라매로 87")
        private String address;

        public static MyStoreResponseDto from(Store store, int starPointAvg) {
            String storeImageUrl = store.getImages().get(0).getImagePath();

            return MyStoreResponseDto.builder()
                    .idx(store.getIdx())
                    .starPoint(starPointAvg)
                    .name(store.getName())
                    .description(store.getDescription())
                    .imagePath(storeImageUrl)
                    .callNumber(store.getCallNumber())
                    .openingHours(store.getOpeningHours())
                    .startTime(store.getStartTime())
                    .endTime(store.getEndTime())
                    .address(store.getAddress())
                    .build();
        }
    }
}
