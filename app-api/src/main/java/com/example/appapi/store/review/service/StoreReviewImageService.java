package com.example.appapi.store.review.service;

import com.example.appapi.product.review.images.model.ProductReviewImages;
import com.example.appapi.product.review.model.ProductReviews;
import com.example.appapi.product.review.model.ProductReviewsDto;
import com.example.appapi.store.review.model.StoreReview;
import com.example.appapi.store.review.model.StoreReviewDto;
import com.example.appapi.store.review.model.StoreReviewImage;
import com.example.appapi.store.review.repository.StoreReviewImageRepository;
import com.example.appapi.upload.PreSignedCloudImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StoreReviewImageService {
    private final StoreReviewImageRepository storeReviewImageRepository;
    private final PreSignedCloudImageService preSignedCloudImageService;
    //@Value("${project.upload.path}")    //

    public StoreReviewDto.ReviewRes preSigned(StoreReviewDto.CreateReq dto, StoreReview storeReview) {

        List<String> uploadFilePaths = new ArrayList();
        List<String> preSignedUrls = new ArrayList();
        for (String file : dto.getImageUrls()) {
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
            String fileName = date + UUID.randomUUID() + "_" + file;
            String preSignedUrl = preSignedCloudImageService.upload(fileName, "image/png");
            preSignedUrls.add(preSignedUrl);
            uploadFilePaths.add(fileName);
        }
        // 이미지 저장 정보를 DB에 저장
        create(uploadFilePaths, storeReview);

        return StoreReviewDto.ReviewRes.of(storeReview,preSignedUrls);
    }


    public void create(List<String> uploadFilePaths, StoreReview storeReview) {
        for(String uploadFilePath: uploadFilePaths) {
            storeReviewImageRepository.save(
                    StoreReviewImage.builder()
                            .url(uploadFilePath)
                            .storeReview(storeReview)
                            .build())
            ;
        }
    }
}