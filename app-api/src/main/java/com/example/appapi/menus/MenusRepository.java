package com.example.appapi.menus;

import com.example.appapi.menus.model.Menus;
import com.example.appapi.store.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MenusRepository extends JpaRepository<Menus, Long> {
    List<Menus> store(Store store);

    List<Menus> findByStoreIdx(Long storeIdx);
}
