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
        private String name;
        private String description;
        private String callNumber;
        private LocalTime startTime;
        private LocalTime endTime;
        private String openingHours;
        private String address;
        private String shortAddress;
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
        private Long idx;
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
        private Long idx;
        private String name;
        private String description;
        private String callNumber;
        private LocalTime startTime;
        private LocalTime endTime;
        private String openingHours;
        private String address;
        private String shortAddress;
        private AllowedStatus allowed;
        private String categoryName;
        @Setter
        List<ClosedDayResponseDto> closedDayList = new ArrayList<>();
        @Setter
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
        private Long idx;
        private String name;
        private String shortAddress;
        private String categoryName;
        private String thumbnail;

        public static StoreSimpleResponseDto from(Store store) {
            return StoreSimpleResponseDto.builder()
                    .idx(store.getIdx())
                    .name(store.getName())
                    .shortAddress(store.getShortAddress())
                    .categoryName(store.getCategory().getName())
                    .thumbnail(store.getImages().isEmpty() ? null : store.getImages().get(0).getImagePath())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StorePageResponseDto<T> {  // DTO만 제네릭화
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
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
        private Long idx;
        private String name;
        private String shortAddress;
        private String categoryName;
        private String thumbnail;
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
