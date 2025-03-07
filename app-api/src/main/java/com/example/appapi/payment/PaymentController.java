package com.example.appapi.payment;

import com.example.appapi.payment.model.Payment;
import com.example.appapi.payment.model.PaymentDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name="결제 관련 기능")
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/payment")
public class PaymentController {
    private final PaymentService paymentService;
    @PostMapping("/register")
    public String register(@RequestBody PaymentDto.PaymentRegister dto) {
        paymentService.register(dto);
        return "결제 등록 완료";
    }

//    @GetMapping("/list")
//    public ResponseEntity<List<PaymentDto.PaymentResponse>> list() {
//        List<PaymentDto.PaymentResponse> response = paymentService.list();
//
//        return ResponseEntity.ok(response);
//    }

//    @GetMapping("/{paymentIdx}")
//    public ResponseEntity<PaymentDto.PaymentResponse> read(@PathVariable Long paymentIdx) {
//        PaymentDto.PaymentResponse response = paymentService.read(paymentIdx);
//
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/cancel/{paymentIdx}")
    public ResponseEntity<Map<String, Object>> cancelPayment(@PathVariable Long paymentIdx) {
        Map<String, Object> response = paymentService.cancelPayment(paymentIdx);

        return ResponseEntity.ok(response);
    }

}
