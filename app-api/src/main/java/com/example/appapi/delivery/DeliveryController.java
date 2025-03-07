package com.example.appapi.delivery;

import com.example.appapi.delivery.model.Delivery;
import com.example.appapi.delivery.model.DeliveryDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RestController
@RequestMapping("/app/delivery")
@Tag(name = "배달 관련 기능")
public class DeliveryController {
    private final DeliveryService deliveryService;
    @Operation(summary = "배송 등록 기능", description = " ")
    @PostMapping("/register")
    public String register(@RequestBody DeliveryDto.RegisterRequest dto) {
        deliveryService.register(dto);
        return "배송 등록 완료";
    }

    @Operation(summary = "배송 목록 조회", description = " ")
    @GetMapping("/list")
    public ResponseEntity<List<DeliveryDto.ListResponse>> list() {
        List<DeliveryDto.ListResponse> resp = deliveryService.getList();
        return ResponseEntity.ok(resp);
    }

    @Operation(summary = "배송 상세조회", description = " ")
    @GetMapping("/{idx}")
    public ResponseEntity<DeliveryDto.ReadResponse> get(@PathVariable Long idx) {
        DeliveryDto.ReadResponse resp = deliveryService.getRead(idx);
        return ResponseEntity.ok(resp);
    }
}
