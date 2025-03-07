package com.example.appapi.orders.orderProducts;

import com.example.appapi.orders.orderProducts.model.OrderProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProductsRepository extends JpaRepository<OrderProducts, Long> {
    //List<OrderProducts> findAllByOrdersIdx(Long ordersIdx);

    @Query("SELECT op FROM OrderProducts op JOIN FETCH op.products WHERE op.orders.idx = :ordersIdx")
    List<OrderProducts> findAllByOrdersIdx(@Param("ordersIdx") Long ordersIdx);

    @Query("SELECT op FROM OrderProducts op " +
            "JOIN FETCH op.orders o " +
            "JOIN FETCH o.user u " +
            "WHERE u.idx = :userIdx " +
            "AND op.reviewStatus = 'Not Reviewed'")
    List<OrderProducts> findPendingReviewsByUser(@Param("userIdx") Long userIdx);

    @Modifying
    @Query("UPDATE OrderProducts op SET op.reviewStatus = 'Reviewed' WHERE op.idx = :orderProductIdx")
    void updateReviewStatus(Long orderProductIdx);
}
