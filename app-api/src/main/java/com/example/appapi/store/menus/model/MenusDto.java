package com.example.appapi.store.menus.model;

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
        @Schema(description = "메뉴 이미지 경로")
        private String imagePath;
        @Schema(description = "메뉴설명", example = "부대찌개는 2인 이상 주문 가능합니다")
        private String info;
        @Schema(description = "메뉴가격", example = "110000")
        private int price;
        @Schema(description = "상점 고유번호", example = "1")
        private Long storeIdx;

        public Menus toEntity(Store store){
            return Menus.builder()
                    .name(name)
                    .imagePath(imagePath)
                    .info(info)
                    .price(price)
                    .store(store)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class MenuListResponseDto {
        @Schema(description = "메뉴 고유번호", example = "1")
        private Long idx;
        @Schema(description = "메뉴명", example = "부대찌개")
        private String name;
        @Schema(description = "메뉴가격", example = "110000")
        private int price;
        @Schema(description = "메뉴설명", example = "부대찌개는 2인 이상 주문 가능합니다")
        private String info;
        public static MenuListResponseDto from(Menus menus) {
            return MenuListResponseDto.builder()
                    .idx(menus.getIdx())
                    .name(menus.getName())
                    .price(menus.getPrice())
                    .info(menus.getInfo())
                    .build();
        }
    }
    @Builder
    @Getter
    public static class DeleteMenuResponse {
        @Schema(description = "삭제한 메뉴 고유번호", example = "1")
        private Long menuIdx;
        @Schema(description = "삭제 완료 메세지", example = "메뉴가 삭제되었습니다.")
        private String message;
        public static DeleteMenuResponse from(Long menuIdx) {
            return DeleteMenuResponse.builder()
                    .menuIdx(menuIdx)
                    .message("메뉴가 삭제되었습니다.")
                    .build();
        }
    }

    @Getter
    public static class DeleteMenuRequest {
        @Schema(description = "삭제할 메뉴 고유번호", example = "1")
        private Long menuIdx;
    }
}
