package com.example.appapi.admin;

import com.example.appapi.category.model.Category;
import com.example.appapi.category.model.CategoryDto;
import com.example.appapi.store.StoreService;
import com.example.appapi.store.model.StoreDto;
import com.example.common.BaseResponse;
import com.example.common.BaseResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/app/admin")
@Tag(name = "관리자 관련 기능")
public class AdminController {
    private final AdminService adminService;


    // 식당 목록 조회
    @Operation(summary = "식당 목록 조회", description = "식당 등록 요청된 모든 목록 조회")
    @GetMapping("/store/list")
    public ResponseEntity<BaseResponse<StoreDto.StorePageResponseDto>> getStoreList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String allowed) {
        StoreDto.StorePageResponseDto response = adminService.storeListAll(page, size, allowed);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, response));
    }

    // 식당 상세 조회
    @Operation(summary = "식당 상세 조회")
    @GetMapping("/store/{storeIdx}")
    public ResponseEntity<BaseResponse<StoreDto.StoreResponseDto>> getStore(@PathVariable Long storeIdx) {
        StoreDto.StoreResponseDto response = adminService.getStore(storeIdx);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, response));
    }

    // 식당 등록 수락 & 카테고리 배정 수정
    @PatchMapping("/store/{storeIdx}")
    @Operation(summary = "식당 등록 수락, 카테고리 배정 수정")
    public ResponseEntity<BaseResponse<StoreDto.StoreResponseDto>> updateStoreStatus(
            @PathVariable Long storeIdx,
            @RequestBody StoreDto.UpdateStoreStatusDto requestDto) {

        StoreDto.StoreResponseDto response = adminService.updateStoreStatus(storeIdx, requestDto);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, response));
    }


    @PostMapping("/category/create")
    @Operation(summary = "카테고리 등록")
    public ResponseEntity<CategoryDto.CategoryResponseDto> createCategory(@RequestBody CategoryDto.CreateCategoryDto dto) {
        CategoryDto.CategoryResponseDto category = adminService.createCategory(dto);
        return ResponseEntity.ok(category);
    }

    // 카테고리 수정
    @PatchMapping("/category/{categoryIdx}")
    @Operation(summary = "카테고리명 수정")
    public ResponseEntity<BaseResponse<CategoryDto.CategoryResponseDto>> updateCategoryName(
            @PathVariable Long categoryIdx,
            @RequestBody CategoryDto.UpdateCategoryRequestDto requestDto) {

        CategoryDto.CategoryResponseDto response = adminService.updateCategoryName(categoryIdx, requestDto.getName());

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, response));
    }

    // 카테고리 삭제
    @DeleteMapping("/category/{categoryIdx}")
    @Operation(summary = "카테고리 삭제", description = "해당 카테고리와 해당 카테고리의 하위 카테고리까지 전부 삭제")
    public ResponseEntity<BaseResponse<Void>> deleteCategoryWithChildren(@PathVariable Long categoryIdx) {
        // 서비스에서 반환되는 BaseResponse를 그대로 응답으로 반환
        BaseResponse<Void> response = adminService.deleteCategoryWithChildren(categoryIdx);

        return ResponseEntity.ok(response);
    }


}
