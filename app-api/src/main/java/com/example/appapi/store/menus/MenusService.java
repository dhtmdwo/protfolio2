package com.example.appapi.store.menus;

import com.example.appapi.store.menus.model.Menus;
import com.example.appapi.store.menus.model.MenusDto;
import com.example.appapi.store.StoreRepository;
import com.example.appapi.store.model.Store;
import com.example.appapi.upload.PreSignedCloudImageService;
import com.example.appapi.users.model.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MenusService {
    private final MenusRepository menusRepository;
    private final StoreRepository storeRepository;
    private final PreSignedCloudImageService preSignedCloudImageService;
    @Transactional
    public MenusDto.MenusResponseDto create(Users user, MenusDto.CreateMenuRequestDto dto) {
        Store store = storeRepository.findByIdAndUserId(dto.getStoreIdx(), user.getIdx());

        String uploadFilePath = null;
        String preSignedUrl = null;

        String file = dto.getImagePath();
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
        String fileName = "menu/" + date + UUID.randomUUID() + "_" + file;
        preSignedUrl = preSignedCloudImageService.upload(fileName, "image/png");
        uploadFilePath = fileName;

        Menus menu = dto.toEntity(store, uploadFilePath);
        menusRepository.save(menu);

        MenusDto.MenusResponseDto response = MenusDto.MenusResponseDto.from(menu);
        response.setImagePath(uploadFilePath);
        response.setImageUrls(preSignedUrl);
        return response;
    }



    public List<MenusDto.MenuListResponseDto> getList(Long storeIdx) {
        List<Menus> menus = menusRepository.findByStoreIdx(storeIdx);
        List<MenusDto.MenuListResponseDto> resp = new ArrayList<>();
        for (Menus menu : menus) {
            resp.add(MenusDto.MenuListResponseDto.from(menu));
        }
        return resp;
    }

    @Transactional
    public MenusDto.DeleteMenuResponse deleteMenu(Long menuIdx, Long userIdx) {
        int deleteCount = menusRepository.deleteByIdxAndUserIdx(menuIdx, userIdx);
        if (deleteCount == 0) {
            throw new IllegalArgumentException();
        }
        return MenusDto.DeleteMenuResponse.from(menuIdx);
    }
}
