package com.example.appapi.admin;

import com.example.appapi.category.CategoryRepository;
import com.example.appapi.category.model.Category;
import com.example.appapi.category.model.CategoryDto;
import com.example.appapi.store.StoreQueryRepository;
import com.example.appapi.store.StoreRepository;
import com.example.appapi.store.model.AllowedStatus;
import com.example.appapi.store.model.Store;
import com.example.appapi.store.model.StoreDto;
import com.example.common.BaseResponse;
import com.example.common.BaseResponseStatus;
import com.example.common.exception.BaseException;
import jakarta.persistence.EntityNotFoundException;
import jdk.swing.interop.SwingInterOpUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final StoreRepository storeRepository;
    private final StoreQueryRepository storeQueryRepository;
    private final CategoryRepository categoryRepository;

    // 관리자 식당 조회
    public StoreDto.StorePageResponseDto<StoreDto.AdminStoreResponse> storeListAll(int page, int size, String allowed) {
        Page<Store> result = storeQueryRepository.searchWithAllowed(page, size, allowed);
        return StoreDto.StorePageResponseDto.from(result, StoreDto.AdminStoreResponse::from);
    }

    public StoreDto.StoreResponseDto getStore(Long storeIdx) {
        Store store = storeRepository.findById(storeIdx).orElseThrow(
                () ->  new BaseException(BaseResponseStatus.STORE_NOT_FOUND)
        );

        return StoreDto.StoreResponseDto.from(store);
    }

    @Transactional
    public StoreDto.StoreResponseDto updateStoreStatus(Long storeIdx, StoreDto.UpdateStoreStatusDto dto) {
        Store store = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.STORE_NOT_FOUND));


        if (!store.getAllowed().equals(dto.getAllowed())) {
            store.setAllowed(dto.getAllowed());
        }

        Category category = categoryRepository.findById(dto.getCategoryIdx())
                .orElseThrow(() -> new BaseException(BaseResponseStatus.CATEGORY_NOT_FOUND));

        if (!store.getCategory().equals(category)) {
            store.setCategory(category);
        }

        storeRepository.save(store);

        return StoreDto.StoreResponseDto.from(store);
    }


    @Transactional
    public CategoryDto.CategoryResponseDto createCategory(CategoryDto.CreateCategoryDto dto) {
        Category parentCategory = null;
        if (dto.getParentIdx() != null) {
            parentCategory = categoryRepository.findById(dto.getParentIdx())
                    .orElseThrow(() -> new BaseException(BaseResponseStatus.CATEGORY_NOT_FOUND_PARENT_CATEGORY));
        }

        Category category = categoryRepository.save(dto.toEntity(parentCategory));

        return CategoryDto.CategoryResponseDto.from(category);
    }


    @Transactional
    public CategoryDto.CategoryResponseDto updateCategoryName(Long categoryIdx, String name) {
        Category category = categoryRepository.findById(categoryIdx)
                .orElseThrow(() -> new EntityNotFoundException("category not found with id: " + categoryIdx));

        category.setName(name);

        categoryRepository.save(category);

        return CategoryDto.CategoryResponseDto.from(category);
    }

    @Transactional
    public BaseResponse<Void> deleteCategoryWithChildren(Long categoryIdx) {
        // categoryIdx와 parentIdx가 각각 Store와 연결된 카테고리인지 확인
        List<Store> storesWithCategory = storeRepository.findByCategory_Idx(categoryIdx);
        List<Store> storesWithParentCategory = storeRepository.findByCategory_ParentCategory_Idx(categoryIdx);

        if (!storesWithCategory.isEmpty() || !storesWithParentCategory.isEmpty()) {
            // Store와 연결된 카테고리는 삭제할 수 없다는 메시지를 반환
            return new BaseResponse<>(BaseResponseStatus.ADMIN_CATEGORY_DELETE_FAILED_REASON_CONNECT_STORE);
        } else {
            // 하위 카테고리 삭제
            List<Category> childCategories = categoryRepository.findByParentCategory_Idx(categoryIdx);
            categoryRepository.deleteAll(childCategories);

            // 카테고리 삭제
            Category category = categoryRepository.findById(categoryIdx)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryIdx));
            categoryRepository.delete(category);

            // 성공적인 삭제 후 응답
            return new BaseResponse<>(BaseResponseStatus.SUCCESS);
        }
    }
}
