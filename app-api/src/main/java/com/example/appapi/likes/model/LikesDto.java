package com.example.appapi.likes.model;

import com.example.appapi.likes.model.Likes;
import com.example.appapi.likes.model.LikesDto;
import com.example.appapi.store.images.model.StoreImages;
import com.example.appapi.store.model.Store;
import com.example.appapi.users.UsersRepository;
import com.example.appapi.users.model.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class LikesDto {
    @Getter
    public static class LikeRegister {
        private Users user;
        private Store store;
        public static Likes toEntity(Users user, Store store) {

            return Likes.builder()
                    .users(user)
                    .store(store)
                    .build();
        }
    }

    @Builder
    @Getter
    public static class StoreLikesResponse {
        private Long idx;               // 식당 idx값
        private String storeImage;      // 식당 이미지 (이미지 테이블)
        private String storeAddress;    // 식당 주소
        private String storeName;       // 식당 이름
        private String storecallNumber; // 식당 번호

        public static LikesDto.StoreLikesResponse from(Store store) {
            String storeImageUrl = "";
            if(store.getImages() != null && !store.getImages().isEmpty()) {
                storeImageUrl = store.getImages().get(0).getImagePath();
            }
            return LikesDto.StoreLikesResponse.builder()
                    .idx(store.getIdx())
                    .storeImage(storeImageUrl)
                    .storeAddress(store.getAddress())
                    .storeName(store.getName())
                    .storecallNumber(store.getCallNumber())
                    .build();
        }
    } // 마이패이지 클라이언트 좋아요 한 식당 내역 보기

    @Builder
    @Getter
    public static class StoreAllLikesResponse{
        private Long storeIdx;
        private String storeName;
        private Long likeCount;

        public static StoreAllLikesResponse from(Store store) {
            //Long likeCount = (long)store.getLikesList().size();
            Long likeCount = store.getLikesCount();
            return LikesDto.StoreAllLikesResponse.builder()
                    .storeIdx(store.getIdx())
                    .storeName(store.getName())
                    .likeCount(likeCount)
                    .build();
        }

    }// 식당 별 좋아요




}
