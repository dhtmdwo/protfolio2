package com.example.appapi.store.review.repository;

import com.example.appapi.store.review.model.StoreReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StoreReviewRepository extends JpaRepository<StoreReview, Long> {
    @Query("SELECT DISTINCT r FROM StoreReview r " +
            "LEFT JOIN FETCH r.storeReviewImageList s " +
            "WHERE r.users.idx = :userId")
    List<StoreReview> findReviewBy(@Param("userId") Long userId);

}