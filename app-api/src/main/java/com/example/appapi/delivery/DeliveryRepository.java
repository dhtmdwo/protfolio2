package com.example.appapi.delivery;

import com.example.appapi.delivery.model.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Delivery findByIdx(Long idx);
}
