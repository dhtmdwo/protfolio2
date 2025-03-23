package com.example.resv.resv;

import com.example.appapi.users.model.Users;
import com.example.common.BaseResponse;
import com.example.common.BaseResponseStatus;
import com.example.resv.resv.model.ResvDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/app/resv")
@Tag(name = "예약 기능")
public class ResvController {
    private final ResvService resvService;

    @Operation(summary = "식당 예약하기 (CLIENT)")
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<ResvDto.ResvResponse>> create(@AuthenticationPrincipal Users user, @RequestBody ResvDto.CreateResvRequest dto) {
        ResvDto.ResvResponse resv = resvService.create(dto, user);

        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, resv));
    }

    @Operation(summary = "예약한 식당 내역 보기 (CLIENT)")
    @GetMapping("/mypage")
    public ResponseEntity<BaseResponse<List<ResvDto.StoreRezResponse>>> getResvList(@AuthenticationPrincipal Users user) {
        List<ResvDto.StoreRezResponse> responseList = resvService.myResvList(user);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, responseList));
    }

    @Operation(summary = "예약 취소하기 (CLIENT)")
    @DeleteMapping("/mypage/delete/{idx}")
    public ResponseEntity<BaseResponse<String>> deleteResv(@PathVariable Long idx) {
        resvService.deleteReservation(idx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, "예약이 성공적으로 취소되었습니다."));
    }

    @Operation(summary = "내 식당의 예약 정보 (SELLER)")
    @GetMapping("/mypage/store/{storeIdx}")
    public ResponseEntity<BaseResponse<ResvDto.ResvPageResponseDto>> getStoreResvList(
            @AuthenticationPrincipal Users user,
            @PathVariable Long storeIdx,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ResvDto.ResvPageResponseDto responseList = resvService.myStoreResv(user, storeIdx, page, size);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, responseList));
    }

    @Operation(summary = "예약한 식당 중 리뷰 작성 가능한 식당 (CLIENT)")
    @GetMapping("/mypage/store/canreview")
    public ResponseEntity<BaseResponse<List<ResvDto.ResvCanReviewResponseDto>>> getCanReviewStoreList(
            @AuthenticationPrincipal Users user) {
        List<ResvDto.ResvCanReviewResponseDto> responseList = resvService.getCanReviewStoreList(user.getIdx());
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS, responseList));
    }
    

}
