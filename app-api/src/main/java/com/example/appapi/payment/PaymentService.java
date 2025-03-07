package com.example.appapi.payment;

import com.example.appapi.orders.OrdersRepository;
import com.example.appapi.orders.OrdersService;
import com.example.appapi.orders.model.Orders;
import com.example.appapi.payment.PaymentRepository;
import com.example.appapi.payment.model.Payment;
import com.example.appapi.payment.model.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Transactional
    public void register(PaymentDto.PaymentRegister dto) {
        Orders orders = ordersRepository.findByIdx(dto.getOrderIdx());
        Payment payment = paymentRepository.save(dto.toEntity(orders.getIdx()));
    }

//    public List<PaymentDto.PaymentResponse> list() {
//        List<Payment> paymentList = paymentRepository.findAll();
//
//        return paymentList.stream().map(PaymentDto.PaymentResponse::from).collect(Collectors.toList());
//    }

//    public PaymentDto.PaymentResponse read(Long paymentIdx) {
//        Payment payment = paymentRepository.findById(paymentIdx).orElseThrow();
//        return PaymentDto.PaymentResponse.from(payment);
//    }

    public Map<String, Object> cancelPayment(Long paymentIdx) {
        Payment payment = paymentRepository.findById(paymentIdx).orElseThrow();
        String txId = payment.getTxId(); //txID 값 알기
        
        String url = "https://api.portone.io/payments/"+txId+"/cancel";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of("reason", "reason");
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        }
        throw new RuntimeException("결제 취소 요청 실패");


    }

}