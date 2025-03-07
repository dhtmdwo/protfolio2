//package com.example.appapi.paymentmethod;
//
//import com.example.appapi.paymentmethod.model.PaymentMethodDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/paymentMethod")
//public class PaymentMethodController {
//    private final PaymentMethodService paymentMethodService;
//    @PostMapping("/register")
//    public String register(@RequestBody PaymentMethodDto.PaymentMethodRegister dto) {
//        paymentMethodService.register(dto);
//        return "결제 등록 완료";
//    }
//
//    @GetMapping("/list")
//    public ResponseEntity<List<PaymentMethodDto.PaymentMethodResponse>> list() {
//        List<PaymentMethodDto.PaymentMethodResponse> response = paymentMethodService.list();
//
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/{paymentMethodIdx}")
//    public ResponseEntity<PaymentMethodDto.PaymentMethodResponse> read(@PathVariable Long paymentMethodIdx) {
//        PaymentMethodDto.PaymentMethodResponse response = paymentMethodService.read(paymentMethodIdx);
//
//        return ResponseEntity.ok(response);
//    }
//
//}
