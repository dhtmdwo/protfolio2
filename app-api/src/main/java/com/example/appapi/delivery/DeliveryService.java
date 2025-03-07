package com.example.appapi.delivery;

import com.example.appapi.delivery.model.Delivery;
import com.example.appapi.delivery.model.DeliveryDto;
import com.example.appapi.orders.model.OrdersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    public void register(DeliveryDto.RegisterRequest dto) {
        deliveryRepository.save(dto.toEntity());
    }

    public List<DeliveryDto.ListResponse> getList() {
        List<Delivery> dto = deliveryRepository.findAll();
        return dto.stream().map(DeliveryDto.ListResponse::from).collect(Collectors.toList());
    }

    public DeliveryDto.ReadResponse getRead(Long idx) {
        Delivery dto = deliveryRepository.findByIdx(idx);
        return DeliveryDto.ReadResponse.from(dto);
    }
}
