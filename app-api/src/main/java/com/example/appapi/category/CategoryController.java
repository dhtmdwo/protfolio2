package com.example.appapi.category;

import com.example.appapi.category.model.Category;
import com.example.appapi.category.model.CategoryDto;
import com.example.common.BaseResponse;
import com.example.common.BaseResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/app/category")
@Tag(name = "카테고리 관련 기능")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 전체 조회", description = "카테고리 전체 조회하는 기능입니다.")
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<CategoryDto.CategoryResponseDto>>> getBigCategory() {
        List<CategoryDto.CategoryResponseDto> bigCategoryList = categoryService.getBigCategory();

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, bigCategoryList));
    }
}
