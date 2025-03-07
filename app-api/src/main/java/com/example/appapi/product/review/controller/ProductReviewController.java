package com.example.appapi.product.review.controller;

import com.example.appapi.product.review.model.ProductReviewsDto;
import com.example.appapi.product.review.service.ProductReviewsService;
import com.example.appapi.store.review.model.StoreReviewDto;
import com.example.appapi.users.model.Users;
import com.example.common.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name ="상품 리뷰 관련 기능")
@RestController
@RequestMapping("/app/products/reviews")
@RequiredArgsConstructor
public class ProductReviewController {
    private final ProductReviewsService productReviewsService;

    @Operation(summary = "작성한 상품 리뷰 보기(클라이언트)")
    @GetMapping("/mypage/store")
    public ResponseEntity<List<ProductReviewsDto.ProductReviewResponse>> storeList
            (@AuthenticationPrincipal Users user)
    {
        List<ProductReviewsDto.ProductReviewResponse> responseList = productReviewsService.productList(user.getIdx());
        return ResponseEntity.ok(responseList);
    } // 마이페이지 클라이언트 식당 리뷰 보기

    @Operation(summary = "작성한 상품 리뷰 삭제 (CLIENT)")
    @GetMapping("/mypage/productdelete/{reviewIdx}")
    public ResponseEntity<String> deleteLikes(@PathVariable Long reviewIdx) {
        productReviewsService.deleteReview(reviewIdx);
        return ResponseEntity.ok("삭제 완료");
    } // 마이페이지 클라이언트 상품 리뷰 삭제

    @Operation(summary = "상품 리뷰 작성하기 (CLIENT)")
    @PostMapping("/create")
    public ResponseEntity<ProductReviewsDto.ReviewRes> create(
            @RequestBody ProductReviewsDto.CreateReq dto,
            @AuthenticationPrincipal Users user) {
        ProductReviewsDto.ReviewRes response = productReviewsService.create(dto, user);
        return ResponseEntity.ok(response);
    } // 상품 리뷰 작성하기

    @Operation(summary = "작성 가능한 리뷰 (CLIENT)")
    @GetMapping("/reviewable")
    public ResponseEntity<List<ProductReviewsDto.ReviewablesResponse>> getReviewables
            (@AuthenticationPrincipal Users user)
    {
        List<ProductReviewsDto.ReviewablesResponse> resp = productReviewsService.getReviewables(user.getIdx());
        return ResponseEntity.ok(resp);
    }
}
