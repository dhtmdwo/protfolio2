package com.example.appapi.category.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {
    @Getter
    public static class CreateCategoryDto {
        private String name;
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
        private Long idx;
        private String name;
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
        private String name;
        private Long idx;

        public Category toEntity() {
            return Category.builder()
                    .name(name)
                    .idx(idx)
                    .build();
        }
    }



}
