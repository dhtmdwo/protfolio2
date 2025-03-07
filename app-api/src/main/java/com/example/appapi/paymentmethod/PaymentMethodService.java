//package com.example.appapi.paymentmethod;
//
//import com.example.appapi.paymentmethod.model.PaymentMethod;
//import com.example.appapi.paymentmethod.model.PaymentMethodDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RequiredArgsConstructor
//@Service
//public class PaymentMethodService {
//    private final PaymentMethodRepository paymentMethodRepository;
//    public void register(PaymentMethodDto.PaymentMethodRegister dto) {
//        PaymentMethod paymentMethod = paymentMethodRepository.save(dto.toEntity());
//    }
//
//    public List<PaymentMethodDto.PaymentMethodResponse> list() {
//        List<PaymentMethod> paymentMethodList = paymentMethodRepository.findAll();
//
//        return paymentMethodList.stream().map(PaymentMethodDto.PaymentMethodResponse::from).collect(Collectors.toList());
//    }
//
//    public PaymentMethodDto.PaymentMethodResponse read(Long paymentMethodIdx) {
//        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentMethodIdx).orElseThrow();
//        return PaymentMethodDto.PaymentMethodResponse.from(paymentMethod);
//    }
//
//}