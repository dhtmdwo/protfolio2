package com.example.appapi.menus.model;

import com.example.appapi.store.model.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

public class MenusDto {
    @Builder
    @Getter
    public static class MenusResponseDto {
        @Schema(description = "메뉴명", example = "부대찌개")
        private String name;
        @Schema(description = "메뉴가격", example = "110000")
        private int price;
        public static MenusResponseDto from(Menus menus) {
            return MenusResponseDto.builder()
                    .name(menus.getName())
                    .price(menus.getPrice())
                    .build();
        }
    }

    @Getter
    public static class CreateMenuRequestDto {
        @Schema(description = "메뉴명", example = "부대찌개")
        private String name;
        @Schema(description = "메뉴가격", example = "110000")
        private int price;
        @Schema(description = "메뉴설명", example = "부대찌개는 2인 이상 주문 가능합니다")
        private String info;
        @Schema(description = "상품 고유번호", example = "1")
        private Long storeIdx;

        public Menus toEntity(Store store){
            return Menus.builder()
                    .name(name)
                    .price(price)
                    .info(info)
                    .store(store)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class MenuListResponseDto {
        @Schema(description = "메뉴명", example = "부대찌개")
        private String name;
        @Schema(description = "메뉴가격", example = "110000")
        private int price;
        @Schema(description = "메뉴설명", example = "부대찌개는 2인 이상 주문 가능합니다")
        private String info;
        public static MenuListResponseDto from(Menus menus) {
            return MenuListResponseDto.builder()
                    .name(menus.getName())
                    .price(menus.getPrice())
                    .info(menus.getInfo())
                    .build();
        }

    }
}
