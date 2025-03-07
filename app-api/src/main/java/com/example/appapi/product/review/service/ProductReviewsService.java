package com.example.appapi.product.review.service;

import com.example.appapi.orders.orderProducts.OrderProductsRepository;
import com.example.appapi.orders.orderProducts.model.OrderProducts;
import com.example.appapi.product.model.Products;
import com.example.appapi.product.repository.ProductsRepository;
import com.example.appapi.product.review.images.model.ProductReviewImages;
import com.example.appapi.product.review.images.service.ProductReviewsImagesService;
import com.example.appapi.product.review.model.ProductReviews;
import com.example.appapi.product.review.model.ProductReviewsDto;
import com.example.appapi.product.review.repository.ProductReviewsRepository;
import com.example.appapi.users.model.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductReviewsService {
    private final ProductReviewsRepository productReviewsRepository;
    private final ProductReviewsImagesService productReviewsImagesService;
    private final ProductsRepository productsRepository;
    private final OrderProductsRepository orderProductsRepository;

    @Transactional
    public ProductReviewsDto.ReviewRes create(ProductReviewsDto.CreateReq dto, Users user) {
        Products products = productsRepository.findById(dto.getProductIdx()).orElseThrow(); //product entity 검색
        OrderProducts orderProducts = orderProductsRepository.findById(dto.getOrderProductIdx()).orElseThrow();

        ProductReviews productReviews = productReviewsRepository.save(dto.toEntity(products, user, orderProducts)); // review 테이블에 저장
        orderProductsRepository.updateReviewStatus(dto.getOrderProductIdx());

        return productReviewsImagesService.preSigned(dto,productReviews); // 업로드와 imageReview 테이블 저장
    } // 상품 리뷰 작성하기

    public int getStarPoint(Long idx) {
        ProductReviews productReviews = productReviewsRepository.findByIdx(idx).orElseThrow();
        return productReviews.getStarPoint();
    }

    public List<ProductReviewsDto.ProductReviewResponse> productList(Long userIdx) {
        List<ProductReviews> productReviews = productReviewsRepository.findReviewByUserIdx(userIdx);

        List<ProductReviewsDto.ProductReviewResponse> responseList = new ArrayList<>();

        for (ProductReviews productReview : productReviews) {
            List<ProductReviewImages> productReviewImageList = productReview.getImages();
            List <String> imageUrls = productReviewImageList.stream().map(ProductReviewImages::getImagePath).toList();
            ProductReviewsDto.ProductReviewResponse response = ProductReviewsDto.ProductReviewResponse.from(productReview, imageUrls);
            responseList.add(response);
        }

        return responseList;
    } // 마이페이지 클라이언트 상품 리뷰 보기

    public void deleteReview(Long idx) {
        productReviewsRepository.findById(idx).ifPresentOrElse(
                productReviewsRepository::delete,
                () -> {
                    throw new IllegalArgumentException("해당리뷰가 존재하지 않습니다");
                }
        );// idx 값으로 검색후 있으면 삭제 없으면 예외처리
    } // 마이페이지 클라이언트 상품 리뷰 삭제


    public List<ProductReviewsDto.ReviewablesResponse> getReviewables(Long userIdx) {
        List<OrderProducts> result = orderProductsRepository.findPendingReviewsByUser(userIdx);
        List<ProductReviewsDto.ReviewablesResponse> resp = new ArrayList<>();
        for (OrderProducts orderProducts : result) {
            String name = orderProducts.getProducts().getName();
            String imageUrl = orderProducts.getProducts().getImages().get(0).getImagePath();    //상품 이미지가 없으면 예외 발생
            Long idx = orderProducts.getIdx();
            resp.add(ProductReviewsDto.ReviewablesResponse.from(name, imageUrl, idx));
        }
        return resp;
    }
}
