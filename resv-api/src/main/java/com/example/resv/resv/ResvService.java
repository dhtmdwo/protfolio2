package com.example.resv.resv;

import com.example.appapi.store.StoreRepository;
import com.example.appapi.store.images.model.StoreImages;
import com.example.appapi.store.model.Store;
import com.example.appapi.users.model.Users;
import com.example.common.BaseResponseStatus;
import com.example.common.exception.BaseException;
import com.example.resv.resv.model.Resv;
import com.example.resv.resv.model.ResvDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ResvService {
    private final ResvRepository resvRepository;
    private final StoreRepository storeRepository;

    @Transactional
    public ResvDto.ResvResponse create(ResvDto.CreateResvRequest dto, Users user) {

        Store store = storeRepository.findById(dto.getStoreIdx())
                .orElseThrow(() ->  new BaseException(BaseResponseStatus.STORE_NOT_FOUND));

        Resv resv = resvRepository.save(dto.toEntity(user, store));

        return ResvDto.ResvResponse.from(resv);
    }

    public List<ResvDto.StoreRezResponse> myResvList(Users user) {
        List<Resv> reservations = resvRepository.findReservationsByUserId(user.getIdx());

        List<ResvDto.StoreRezResponse> responseList = new ArrayList<>();

        for (Resv reservation : reservations) {
            Store store = reservation.getStore();
            ResvDto.StoreRezResponse response = ResvDto.StoreRezResponse.from(store, reservation);
            responseList.add(response);
        }

        return responseList;
    } // 마이페이지 클라이언트 예약한 식당 내역 보기

    public void deleteReservation(Long idx) {
        resvRepository.findById(idx).ifPresentOrElse(
                resvRepository::delete,
                () -> {
                    throw new BaseException(BaseResponseStatus.RESERVATION_UPDATE_FAILED);
                }
        );// idx 값으로 검색후 있으면 삭제 없으면 예외처리
    } // 마이페이지 클라이언트 예약 취소

    public ResvDto.ResvPageResponseDto myStoreResv(Users user, Long storeIdx, int page, int size) {
        Store store = storeRepository.findById(storeIdx)
                .orElseThrow(() -> new BaseException(BaseResponseStatus.STORE_NOT_FOUND));

        if(store.getUser().getIdx() != user.getIdx()) {
            throw new BaseException(BaseResponseStatus.RESERVATION_HISTORY_LOOKUP_FAILED);
        }

        Page<Resv> result = resvRepository.findAllByStore(storeIdx, PageRequest.of(page, size));
        return ResvDto.ResvPageResponseDto.from(result);
    }

    public List<ResvDto.ResvCanReviewResponseDto> getCanReviewStoreList(Long userIdx) {
        List<Resv> reservations = resvRepository.findReservationsByUserId(userIdx);

        List<ResvDto.ResvCanReviewResponseDto> responseList = new ArrayList<>();

        for (Resv reservation : reservations) {
            if("NO".equals(reservation.getIsReviewed())) {
                Store store = reservation.getStore();
                List<StoreImages> images = store.getImages();
                String storeImage = "default-image.png";
                if(images !=null && images.size()>0) {
                    storeImage = images.get(0).getImagePath();
                }
                ResvDto.ResvCanReviewResponseDto response = ResvDto.ResvCanReviewResponseDto.from(reservation, store, storeImage);
                responseList.add(response);
            }
        }
        return responseList;
    } // 예약 한 식당 중 리뷰 가능한 식당

}
