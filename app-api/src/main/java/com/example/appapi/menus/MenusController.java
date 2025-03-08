package com.example.appapi.menus;

import com.example.appapi.menus.model.MenusDto;
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
@Tag(name = "메뉴 기능", description = "메뉴 관련 기능")
@RequestMapping("/app/menu")
public class MenusController {
    private final MenusService menusService;
    @Operation(summary = "메뉴 생성하기", description = "내 식당 메뉴 생성하기")
    @PostMapping("/create")
    public ResponseEntity<BaseResponse<MenusDto.MenusResponseDto>> createMenu(
            @AuthenticationPrincipal Users user,
            @RequestBody MenusDto.CreateMenuRequestDto dto){
        MenusDto.MenusResponseDto resp = menusService.create(user, dto);
        return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, resp));
    }
    @Operation(summary = "메뉴 목록보기", description = "내 식당 메뉴 목록 보기")
    @GetMapping("/list/{storeIdx}")
    public ResponseEntity<BaseResponse<MenusDto.MenuListResponseDto>> getList(@PathVariable Long storeIdx){
        List<MenusDto.MenuListResponseDto> response = menusService.getList(storeIdx);

        return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, response));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<BaseResponse<MenusDto.DeleteMenuResponse>> deleteMenu(
            @AuthenticationPrincipal Users user,
            @RequestBody MenusDto.DeleteMenuRequest dto
    ){
        MenusDto.DeleteMenuResponse resp = menusService.deleteMenu(dto.getMenuIdx(), user.getIdx());
        try {
            return ResponseEntity.ok(new BaseResponse(BaseResponseStatus.SUCCESS, resp));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new BaseResponse<>(BaseResponseStatus.ACCOUNT_UPDATE_FAILED));
        }
    }
}
