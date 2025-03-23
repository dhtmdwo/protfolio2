package com.example.resv.resv;

import com.example.resv.resv.model.Resv;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ResvRepository extends JpaRepository<Resv, Long> {

    @Query("SELECT DISTINCT r FROM Resv r " +
            "JOIN FETCH r.store s " +
            "LEFT JOIN FETCH s.images si " +
            "WHERE r.user.idx = :userId")
    List<Resv> findReservationsByUserId(@Param("userId") Long userId);

    @Query("SELECT r FROM Resv r " +
            "WHERE r.store.idx = :storeIdx")
    Page<Resv> findAllByStore(Long storeIdx, PageRequest pr);



}
