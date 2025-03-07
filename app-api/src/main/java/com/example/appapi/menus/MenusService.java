package com.example.appapi.menus;

import com.example.appapi.menus.model.Menus;
import com.example.appapi.menus.model.MenusDto;
import com.example.appapi.store.StoreRepository;
import com.example.appapi.store.StoreService;
import com.example.appapi.store.model.Store;
import com.example.appapi.users.model.Users;
import com.example.common.BaseResponseStatus;
import com.example.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MenusService {
    private final MenusRepository menusRepository;
    private final StoreRepository storeRepository;
    public MenusDto.MenusResponseDto create(Users user, MenusDto.CreateMenuRequestDto dto) {
        Store store = storeRepository.findByIdAndUserId(dto.getStoreIdx(), user.getIdx()).orElseThrow(); //-> new BaseException(BaseResponseStatus.MENU_REGIST_FAILED));

        Menus menu = menusRepository.save(dto.toEntity(store));

        return MenusDto.MenusResponseDto.from(menu);
    }

    public List<MenusDto.MenuListResponseDto> getList(Long storeIdx) {
        List<Menus> menus = menusRepository.findByStoreIdx(storeIdx);
        List<MenusDto.MenuListResponseDto> resp = new ArrayList<>();
        for (Menus menu : menus) {
            resp.add(MenusDto.MenuListResponseDto.from(menu));
        }
        return resp;
    }
}
