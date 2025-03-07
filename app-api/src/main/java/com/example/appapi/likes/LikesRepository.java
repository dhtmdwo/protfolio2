package com.example.appapi.likes;

import com.example.appapi.likes.model.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

import java.util.List;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Query("SELECT DISTINCT l FROM Likes l " +
            "JOIN FETCH  l.store s " +
            "LEFT JOIN FETCH s.images si " +
            "WHERE  l.users.idx = :userId")
    List<Likes> findLikesByUserId(@Param("userId") Long userId);

    @Query("SELECT DISTINCT l FROM Likes l " +
            "WHERE l.users.idx = :userId AND l.store.idx= :storeId")
    Optional<Likes> findLikesByUserIdANDStoreId(@Param("userId") Long userId, @Param("storeId") Long storeId);


    @Query("SELECT COUNT(l) FROM Likes l WHERE l.store.idx = :storeId")
    Long countLikesByStoreId(@Param("storeId") Long storeId);


}
