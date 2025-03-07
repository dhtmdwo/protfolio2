package com.example.appapi.store.review.repository;

import com.example.appapi.store.review.model.StoreReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreReviewImageRepository extends JpaRepository<StoreReviewImage, Long> {
    void deleteByStoreReviewIdx(Long storeReviewIdx);
}