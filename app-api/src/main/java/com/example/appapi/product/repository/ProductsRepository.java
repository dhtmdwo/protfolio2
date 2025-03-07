package com.example.appapi.product.repository;


import com.example.appapi.product.model.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    @Query("select p from Products p join fetch p.reviews where p.idx = :idx")
    Optional<Products> findByIdxWithReviewsAndImages(Long idx);

    Products findByIdx(Long idx);

    Page<Products> findByCategory_Idx(Long categoryIdx, Pageable pageable);
}
