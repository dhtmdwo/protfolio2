package com.example.appapi.orders.orderProducts;

import com.example.appapi.orders.orderProducts.model.OrderProductsDto;
import com.example.appapi.users.model.Users;
import com.example.common.BaseResponse;
import com.example.common.BaseResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Tag(name = "주문 상품 기능", description = "주문 예정 상품 기능")
@RequestMapping("/app/orderproducts")
public class OrderProductsController {
    private final OrderProductsService orderProductsService;
    @Operation(summary = "주문서 작성하기", description = "주문서에서 결제하기 클릭 시 동작")
    @PostMapping("/orderregister")      // 장바구니 -> 주문서 작성 페이지 이동 시 장바구니에 있던 상품들 DB 등록
    public ResponseEntity<BaseResponse<OrderProductsDto.OrderRegisterResponse>> register
            (@RequestBody OrderProductsDto.OrderRegisterRequest dto,
             @AuthenticationPrincipal Users user) {
        OrderProductsDto.OrderRegisterResponse resp = orderProductsService.register(dto, user);
        return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, resp));
    }
    @Operation(summary = "주문할 상품 목록", description = "주문서에서 주문할 상품 목록 보기")
    @GetMapping("/list/{ordersIdx}")    // 주문서 작성 페이지에서 주문할 상품 목록
    public ResponseEntity<BaseResponse<OrderProductsDto.ListProductsResponse>> list(@PathVariable Long ordersIdx) {
        List<OrderProductsDto.ListProductsResponse> resp = orderProductsService.list(ordersIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, resp));
    }
}
