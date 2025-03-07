package com.example.appapi.store;

import com.example.appapi.store.model.StoreClosedDay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreClosedDayRepository extends JpaRepository<StoreClosedDay, Long> {
}
