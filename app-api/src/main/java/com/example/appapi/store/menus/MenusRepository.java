package com.example.appapi.store.menus;

import com.example.appapi.store.menus.model.Menus;
import com.example.appapi.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface MenusRepository extends JpaRepository<Menus, Long> {
    List<Menus> store(Store store);

    List<Menus> findByStoreIdx(Long storeIdx);

    @Modifying
    @Query("DELETE FROM Menus m " +
            "WHERE m.idx = :menuIdx " +
            "AND m.store.user.idx = :userIdx")
    int deleteByIdxAndUserIdx(@Param("menuIdx") Long menuIdx, @Param("userIdx") Long userIdx);
}
