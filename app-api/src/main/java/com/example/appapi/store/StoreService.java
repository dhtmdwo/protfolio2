package com.example.appapi.store;

import com.example.appapi.category.CategoryRepository;
import com.example.appapi.category.model.Category;
import com.example.appapi.product.model.Products;
import com.example.appapi.product.model.ProductsDto;
import com.example.appapi.store.images.StoreImagesService;
import com.example.appapi.store.model.AllowedStatus;
import com.example.appapi.store.model.Store;
import com.example.appapi.store.model.StoreClosedDay;
import com.example.appapi.store.model.StoreDto;
import com.example.appapi.upload.PreSignedCloudImageService;
import com.example.appapi.users.model.Users;
import com.example.common.BaseResponseStatus;
import com.example.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final StoreClosedDayRepository storeClosedDayRepository;
    private final CategoryRepository categoryRepository;
    private final StoreQueryRepository storeQueryRepository;
    private final StoreImagesService storeImagesService;
    private final PreSignedCloudImageService preSignedCloudImageService;

    @Transactional
    public StoreDto.StoreResponseDto preSigned(StoreDto.CreateStoreRequestDto dto, Users user) {
        Category category = categoryRepository.findById(dto.getCategoryIdx()).orElseThrow(
                () ->  new BaseException(BaseResponseStatus.STORE_REGIST_FAILED)
        );

        Store store = storeRepository.save(dto.toEntity(user, category));

        // 식당 휴무일 저장
        List<StoreClosedDay> closedDays = dto.getClosedDayList().stream()
                .map(closedDayRequestDto -> closedDayRequestDto.toEntity(store))
                .toList();

        storeClosedDayRepository.saveAll(closedDays);

        // 식당 휴무일 Response 처리
        List<StoreDto.ClosedDayResponseDto> closedDayResponseList = closedDays.stream()
                .map(StoreDto.ClosedDayResponseDto::from)
                .toList();

        List<String> uploadFilePaths = new ArrayList();
        List<String> preSignedUrls = new ArrayList();
        for (String file : dto.getImagePaths()) {
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
            String fileName = "store/" + date + UUID.randomUUID() + "_" + file;
            String preSignedUrl = preSignedCloudImageService.upload(fileName, "image/png");
            preSignedUrls.add(preSignedUrl);
            uploadFilePaths.add(fileName);
        }
        // 이미지 저장 정보를 DB에 저장
        storeImagesService.create(uploadFilePaths, store);


        StoreDto.StoreResponseDto response = StoreDto.StoreResponseDto.from(store);
        response.setClosedDayList(closedDayResponseList);
        response.setImagePaths(uploadFilePaths);

        return response;
    }

    public StoreDto.StorePageResponseDto<StoreDto.StoreSimpleResponseDto> list(int page, int size, String sort, Long categoryIdx) {
        Page<Store> result = storeQueryRepository.search(page, size, sort, categoryIdx);

        result.forEach(store -> {
            store.updateReviewStatus();
            storeRepository.save(store);    // 최근 리뷰 총 개수, 평균 별점 반영
        });

        return StoreDto.StorePageResponseDto.from(result, StoreDto.StoreSimpleResponseDto::from);
    }

    public StoreDto.StoreResponseDto getStore(Long storeIdx) {
        Store store = storeRepository.findByIdWithClosedDaysAndUserAndCategory(storeIdx).orElseThrow(
                () ->  new BaseException(BaseResponseStatus.STORE_NOT_FOUND)
        );

        store.updateReviewStatus();
        storeRepository.save(store);    // 최근 리뷰 총 개수, 평균 별점 반영

        return StoreDto.StoreResponseDto.from(store);
    }

    public List<StoreDto.MyStoreResponseDto> getMyStore(Long userIdx) {
        // 상점만 존재, 이미지 없을 시 조회 안됨
        List<Store> stores = storeRepository.findAllByUserIdx(userIdx);
        if(stores.isEmpty()) {
            throw new BaseException(BaseResponseStatus.STORE_NOT_FOUND);
        }
        List<StoreDto.MyStoreResponseDto> response = new ArrayList<>();
        for (Store store : stores) {
            Double starPointAvg = storeRepository.findAvgStarPointByStoreIdx(store.getIdx());
            response.add(StoreDto.MyStoreResponseDto.from(store, (int) Math.round(starPointAvg)));
        }
        return response;
    }

}


