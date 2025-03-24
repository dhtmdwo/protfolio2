package com.example.appapi.orders;

import com.example.appapi.orders.model.OrdersDto;
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
@Tag(name = "주문 기능", description = "주문서 관련 기능")
@RequestMapping("/app/orders")
public class OrdersController {
    private final OrdersService ordersService;
    @Operation(summary = "주문서 작성하기", description = "장바구니에서 상품 주문하기 클릭")
    @PostMapping("/register/{idx}")     // 주문서 작성하기
    public ResponseEntity<BaseResponse<OrdersDto.OrdersRegisterResponse>> register(@PathVariable Long idx, @RequestBody OrdersDto.OrdersRegister dto){
        OrdersDto.OrdersRegisterResponse resp = ordersService.register(idx, dto);
        return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, resp));
    }

    @Operation(summary = "내 주문 보기", description = "내 주문 목록 보기")
    @GetMapping("/orderlist")   // 내 주문 보기
    public ResponseEntity<BaseResponse<OrdersDto.OrdersResponse>> orderList(@AuthenticationPrincipal Users user) {
        List<OrdersDto.OrdersResponse> resp = ordersService.getOrderList(user.getIdx());
        return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, resp));
    }

    @GetMapping("/mypage/orderlist")
    public ResponseEntity<BaseResponse<List<OrdersDto.OrderMypageList>>> storeList(@AuthenticationPrincipal Users user) {

        List<OrdersDto.OrderMypageList> responseList = ordersService.orderList(user.getIdx());
        return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, responseList));
    } // 마이페이지 클라이언트 주문 리스트 보기

    @GetMapping("/mypage/orderdetails")
    public ResponseEntity<BaseResponse<OrdersDto.OrderMypageDetails>> storeDetails(@AuthenticationPrincipal Users user, @RequestParam("orderIdx") Long orderIdx) {

        OrdersDto.OrderMypageDetails response = ordersService.orderDetails(user.getIdx(), orderIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, response));
    } // 마이페이지 클라이언트 주문 상세 보기

    @Operation(summary = "주문 상세보기", description = "내 주문 상세 보기")
    @GetMapping("/{orderIdx}")  // 주문 상세보기
    public ResponseEntity<BaseResponse<OrdersDto.OrdersResponse>> orderRead(@PathVariable Long orderIdx){
        OrdersDto.OrdersResponse resp = ordersService.getOrderRead(orderIdx);
        return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, resp));
    }
}
