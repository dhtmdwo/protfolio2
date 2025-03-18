package com.example.appapi.store.review.controller;

import com.example.appapi.store.review.model.StoreReviewDto;
import com.example.appapi.store.review.service.StoreReviewService;
import com.example.appapi.users.model.Users;
import com.example.common.BaseResponse;
import com.example.common.BaseResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name="식당 리뷰 관련 기능")
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/review")
public class StoreReviewController {
    private final StoreReviewService storeReviewService;

    @Operation(summary = "리뷰 작성하기 (CLIENT)", description = "마이 페이지에서 리뷰를 작성하는 기능")
    @PostMapping("/create")
    public ResponseEntity<StoreReviewDto.ReviewRes> create(
            @RequestBody StoreReviewDto.CreateReq dto,
            @AuthenticationPrincipal Users user) {
        StoreReviewDto.ReviewRes response = storeReviewService.create(dto, user);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "식당의 리뷰 목록 조회", description = "식당 정보에서 리뷰를 클릭하여 작성된 리뷰 목록을 보는 기능")
    @GetMapping("/list")
    public ResponseEntity<List<StoreReviewDto.ReviewRes>> list() {
        List<StoreReviewDto.ReviewRes> response = storeReviewService.getList();
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "작성한 식당 리뷰 보기 (CLIENT)")
    @GetMapping("/mypage/store")
    public ResponseEntity<BaseResponse<List<StoreReviewDto.StoreReivewResponse>>> storeList(
            @AuthenticationPrincipal Users users
    )
    {
        Long userIdx = users.getIdx();
        List<StoreReviewDto.StoreReivewResponse> responseList = storeReviewService.storeList(userIdx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, responseList));
    }

    @Operation(summary = "식당 리뷰 삭제하기 (CLIENT)", description = "고객이 자신이 작성한 식당 리뷰를 삭제하는 기능")
    @DeleteMapping("/mypage/storedelete/{reviewIdx}")
    public ResponseEntity<BaseResponse<String>> deleteLikes(@PathVariable Long reviewIdx) {
        storeReviewService.deleteReview(reviewIdx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS,"삭제 완료"));
    } // 마이페이지 클라이언트 식당 리뷰 삭제      

    
      
}
