package com.example.appapi.orders;

import com.example.appapi.orders.model.Orders;
import com.example.appapi.payment.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    Orders findByIdx(Long idx);

    @Query("SELECT DISTINCT o FROM Orders o " +
            "JOIN FETCH o.orderProducts op " +
            "JOIN FETCH op.products p " +
            "LEFT JOIN FETCH o.delivery " +   // 추가
            "WHERE o.user.idx = :userIdx")
    List<Orders> findAllWithOrderProductsAndProducts(@Param("userIdx") Long userIdx);

    @Query("SELECT DISTINCT o FROM Orders o " +
            "JOIN FETCH o.orderProducts op " +
            "JOIN FETCH op.products p " +
            "WHERE o.idx = :ordersIdx")
    Optional<Orders> findByIdWithOrderProductsAndProducts(@Param("ordersIdx") Long ordersIdx);


    @Modifying
    @Query("UPDATE Orders o SET o.price = :totalPrice WHERE o.idx = :orderIdx")
    int updateOrderPrice(@Param("orderIdx") Long orderId, @Param("totalPrice") int totalPrice);

    @Modifying
    @Query("UPDATE Orders o SET o.message = :message, o.status = 'Paying' WHERE o.idx = :orderIdx")
    int updateOrderMessageAndStatus(@Param("orderIdx") Long orderId,
                                    @Param("message") String message);


    @Query("SELECT DISTINCT o FROM Orders o " +
            "JOIN FETCH o.orderProducts op " +
            "JOIN FETCH op.products p " +
            "WHERE o.user.idx = :userId")
    List<Orders> findMyAllOrders(@Param("userId") Long userId); // 마이페이지 클라이언트 주문 목록 보기

    @Query("SELECT DISTINCT o FROM Orders o " +
            "LEFT JOIN FETCH o.orderProducts op " +
            "LEFT JOIN FETCH op.products pr " +
            "LEFT JOIN FETCH o.user u " +
            "LEFT JOIN FETCH o.delivery d " +
            "WHERE o.user.idx = :userId AND o.idx = :orderId"
    )
    Orders findOrderWithDetails(@Param("userId") Long userId, @Param("orderId") Long orderId); // 마이페이지 클라이언트 주문 상세 보기

}
