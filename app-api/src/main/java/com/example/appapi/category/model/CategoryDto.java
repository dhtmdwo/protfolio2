package com.example.appapi.category.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {
    @Getter
    public static class CreateCategoryDto {
        @Schema(description = "카테고리 이름", example = "찜/탕")
        private String name;
        @Schema(description = "부모 카테고리 번호", example = "1")
        private Long parentIdx;

        public Category toEntity(Category parentCategory) {
            return Category.builder()
                    .name(name)
                    .parentCategory(parentCategory)  // 부모 카테고리를 직접 전달받음
                    .build();
        }
    }

    @Getter
    @Builder
    public static class CategoryResponseDto {
        @Schema(description = "카테고리 번호", example = "1")
        private Long idx;
        @Schema(description = "카테고리 이름", example = "한식")
        private String name;
        @Schema(description = "자식 카테고리 리스트")
        private List<CategoryResponseDto> childrenCategoryList;

        public static CategoryResponseDto from(Category category) {
            return CategoryResponseDto.builder()
                    .idx(category.getIdx())
                    .name(category.getName())
                    .childrenCategoryList(
                            category.getChildCategoryList() != null
                                    ? category.getChildCategoryList().stream()
                                    .map(CategoryResponseDto::from)
                                    .toList()
                                    : new ArrayList<>()
                    )
                    .build();
        }

    }

    @Getter
    public static class UpdateCategoryRequestDto {
        @Schema(description = "카테고리 이름", example = "중식")
        private String name;
        @Schema(description = "카테고리 번호", example = "2")
        private Long idx;

        public Category toEntity() {
            return Category.builder()
                    .name(name)
                    .idx(idx)
                    .build();
        }
    }



}
