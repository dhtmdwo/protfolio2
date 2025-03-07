package com.example.appapi.store.review.service;

import com.example.appapi.store.StoreRepository;
import com.example.appapi.store.model.Store;
import com.example.appapi.store.review.model.StoreReview;
import com.example.appapi.store.review.model.StoreReviewDto;
import com.example.appapi.store.review.model.StoreReviewImage;
import com.example.appapi.store.review.repository.StoreReviewRepository;
import com.example.appapi.users.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StoreReviewService {
    private final StoreReviewRepository storeReviewRepository;
    private final StoreReviewImageService storeReviewImageService;
    private final StoreRepository storeRepository;

    public StoreReviewDto.ReviewRes create(StoreReviewDto.CreateReq dto, Users user) {
        Store store = storeRepository.findById(dto.getStoreIdx()).orElseThrow();

        StoreReview storeReview = storeReviewRepository.save(dto.toEntity(store));

        //List<String> uploadFilePaths = storeReviewImageService.upload(files, storeReview);

        return storeReviewImageService.preSigned(dto, storeReview);
    }

    public List<StoreReviewDto.ReviewRes> getList() {
        List<StoreReview> storeReviewList = storeReviewRepository.findAll();
        return storeReviewList.stream()
                .map(review -> StoreReviewDto.ReviewRes.of(review, review.getStoreReviewImageList().stream().map(StoreReviewImage::getUrl).toList()))
                .toList();
    }

    public StoreReviewDto.ReviewRes get(Long reviewIdx) {
        StoreReview storeReview = storeReviewRepository.findById(reviewIdx).orElseThrow();
        return StoreReviewDto.ReviewRes.of(storeReview, storeReview.getStoreReviewImageList().stream().map(StoreReviewImage::getUrl).toList());
    }

    public List<StoreReviewDto.StoreReivewResponse> storeList(Long userIdx) {
        List<StoreReview> storeReviews = storeReviewRepository.findReviewBy(userIdx);

        List<StoreReviewDto.StoreReivewResponse> responseList = new ArrayList<>();

        for (StoreReview storeReview : storeReviews) {
            List<StoreReviewImage> storeReviewImageList = storeReview.getStoreReviewImageList();
            List <String> imageUrls = storeReviewImageList.stream().map(StoreReviewImage::getUrl).toList();
            StoreReviewDto.StoreReivewResponse response = StoreReviewDto.StoreReivewResponse.from(storeReview, imageUrls);
            responseList.add(response);
        }

        return responseList;
    } // 마이페이지 클라이언트 식당 리뷰 보기

    public void deleteReview(Long idx) {
        StoreReview storeReview = storeReviewRepository.findById(idx).orElseThrow(
                () -> new IllegalArgumentException("해당 리뷰가 존재하지 않습니다")
        );

        Store store = storeReview.getStore();
        storeReviewRepository.delete(storeReview);
        store.getStoreReviewList().remove(storeReview);
        store.updateReviewStatus();
        storeRepository.save(store);
    }

}

