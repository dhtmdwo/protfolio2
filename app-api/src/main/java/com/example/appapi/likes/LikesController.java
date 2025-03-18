package com.example.appapi.likes;

import com.example.appapi.likes.model.LikesDto;
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

@Tag(name="좋아요 관련 기능")
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/likes")
public class LikesController {
    private final LikesService likesService;

    @Operation(summary = "좋아요 한 식당 보기 (CLIENT)")
    @GetMapping("/mypage/store")
    public ResponseEntity<BaseResponse<List<LikesDto.StoreLikesResponse>>> storeList(@AuthenticationPrincipal Users users) {
        Long idx = users.getIdx();
        List<LikesDto.StoreLikesResponse> responseList = likesService.storeList(idx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS,responseList));
    } // 마이페이지 클라이언트 좋아요 한 식당 내역 보기

    @Operation(summary = "식당 좋아요(취소 포함) (CLIENT)")
    @PostMapping("/mypage/updatestore/{storeIdx}")
    public ResponseEntity<BaseResponse<String>> updateLikes(@AuthenticationPrincipal Users users, @PathVariable Long storeIdx) {
        Long userIdx = users.getIdx();
        likesService.updateLikes(userIdx, storeIdx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS,"작업 완료"));
    } // 마이페이지 클라이언트 식당 좋아요 삭제


    @Operation(summary = "식당 좋아요 개수 세기")
    @GetMapping("/mypage/count/{storeIdx}")
    public ResponseEntity<BaseResponse<Long>> countLikes(@PathVariable Long storeIdx) {
        Long count = likesService.countLikes(storeIdx);
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS,count));
    } // 마이페이지 클라이언트 식당 좋아요 삭제

    @Operation(summary = "전체 식당별 좋아요 개수 세기")
    @GetMapping("/mypage/countall")
    public ResponseEntity<BaseResponse<List<LikesDto.StoreAllLikesResponse>>> countLikes() {
        List<LikesDto.StoreAllLikesResponse> ressponse = likesService.countLikes();
        return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.SUCCESS,ressponse));
    } // 마이페이지 클라이언트 식당 좋아요 삭제

}
