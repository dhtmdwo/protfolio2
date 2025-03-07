package com.example.appapi.likes;

import com.example.appapi.likes.model.Likes;
import com.example.appapi.likes.model.LikesDto;
import com.example.appapi.store.StoreRepository;
import com.example.appapi.store.model.Store;
import com.example.appapi.users.UsersRepository;
import com.example.appapi.users.model.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class LikesService {
    private final LikesRepository likesRepository;
    private final UsersRepository usersRepository;
    private final StoreRepository storeRepository;

    public List<LikesDto.StoreLikesResponse> storeList(Long idx) {
        List<Likes> likes = likesRepository.findLikesByUserId(idx);

        List<LikesDto.StoreLikesResponse> responseList = new ArrayList<>();

        for (Likes like : likes) {
            Store store = like.getStore();
            LikesDto.StoreLikesResponse response = LikesDto.StoreLikesResponse.from(store);
            responseList.add(response);
        }

        return responseList;
    } // 마이페이지 좋아요 한 식당 내역 보기

    public void deleteLikes(Long userIdx, Long storeIdx) {

        Optional<Likes> likes = likesRepository.findLikesByUserIdANDStoreId(userIdx,storeIdx);

        likes.ifPresentOrElse(
                likesRepository::delete,
                () -> {
                    Users user = usersRepository.findById(userIdx)
                            .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
                    Store store = storeRepository.findById(storeIdx)
                            .orElseThrow(() -> new IllegalArgumentException("가게를 찾을 수 없습니다."));

                    Likes newLike = LikesDto.LikeRegister.toEntity(user, store);
                    likesRepository.save(newLike);
                }
        );

    } // 마이페이지 클라이언트 식당 좋아요 삭제

    public Long countLikes(Long storeIdx) {
        Long count = likesRepository.countLikesByStoreId(storeIdx);
        return count;
    }

    public List<LikesDto.StoreAllLikesResponse> countLikes() {
        List<Store> storeList = storeRepository.findAll();
        List<LikesDto.StoreAllLikesResponse> StoreAllLikesResponseList = new ArrayList<>();

        for(Store store : storeList) {
            LikesDto.StoreAllLikesResponse storeAllLikesResponse = LikesDto.StoreAllLikesResponse.from(store);
            StoreAllLikesResponseList.add(storeAllLikesResponse);
        }


        return StoreAllLikesResponseList;
    }

    
}
