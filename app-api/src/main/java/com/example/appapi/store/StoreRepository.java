package com.example.appapi.store;

import com.example.appapi.store.model.AllowedStatus;
import com.example.appapi.store.model.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s FROM Store s " +
            "JOIN FETCH s.closedDayList cd " +
            "JOIN FETCH s.user u " +
            "JOIN FETCH s.category c " +
            "JOIN FETCH s.storeReviewList " +
            "WHERE s.allowed = :allowedStatus")
    Page<Store> findByAllowed(AllowedStatus allowedStatus, PageRequest of);

    @Query("SELECT s FROM Store s " +
            "JOIN FETCH s.closedDayList cd " +
            "JOIN FETCH s.user u " +
            "JOIN FETCH s.category c " +
            "WHERE s.idx = :idx")
    Optional<Store> findByIdWithClosedDaysAndUserAndCategory(Long idx);

    @Query("SELECT s FROM Store s " +
            "JOIN FETCH s.images i " +
            "JOIN FETCH s.category c " +
            "WHERE s.user.idx = :userIdx")
    List<Store> findAllByUserIdx(@Param("userIdx") Long userIdx);
  
    List<Store> findByCategory_Idx(Long categoryIdx);
  
    List<Store> findByCategory_ParentCategory_Idx(Long parentIdx);

    @Query("SELECT s FROM Store s " +
            "WHERE s.idx = :storeIdx " +
            "AND s.user.idx = :userIdx")
    Optional<Store> findByIdAndUserId(@Param("storeIdx") Long storeIdx, @Param("userIdx") Long userIdx);

//    List<Store> findAll();
    @Query("SELECT AVG(sr.starPoint) " +
            "FROM StoreReview sr " +
            "WHERE sr.store.idx = :storeIdx")
    Double findAvgStarPointByStoreIdx(@Param("storeIdx") Long storeIdx);
}
