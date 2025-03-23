package com.example.appapi.product.service;

import com.example.appapi.category.CategoryService;
import com.example.appapi.product.ProductQueryRepository;
import com.example.appapi.product.images.service.ProductsImagesService;
import com.example.appapi.product.model.Products;
import com.example.appapi.product.model.ProductsDto;
import com.example.appapi.product.repository.ProductsRepository;
import com.example.appapi.product.review.model.ProductReviewsDto;
import com.example.appapi.upload.PreSignedCloudImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsService {
    private final ProductsRepository productsRepository;
    private final ProductQueryRepository productQueryRepository;
    private final ProductsImagesService productsImagesService;
    private final PreSignedCloudImageService preSignedCloudImageService;

    public ProductsDto.ProductRes preSigned(ProductsDto.Create dto) {
        // 상품 정보 DB에 저장
        Products product = productsRepository.save(dto.toEntity());

        List<String> uploadFilePaths = new ArrayList();
        List<String> preSignedUrls = new ArrayList();
        for (String file : dto.getImagePaths()) {
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
            String fileName = date + UUID.randomUUID() + "_" + file;
            String preSignedUrl = preSignedCloudImageService.upload(fileName, "image/png");
            preSignedUrls.add(preSignedUrl);
            uploadFilePaths.add(fileName);
        }
        // 이미지 저장 정보를 DB에 저장
        productsImagesService.create(uploadFilePaths, product);

        ProductsDto.ProductRes response =  ProductsDto.ProductRes.fromEntity(product);
        response.setImageUrls(preSignedUrls);

        return response;
    }

    //products -> images batch size 있어야 함
    public Page<ProductsDto.InfoResponse> listWithCategoryOptimized(int page, int size, String sort, Long categoryIdx) {
        return productQueryRepository.search(page, size, sort, categoryIdx);
    }

    //products -> images batch size X
    public Page<ProductsDto.InfoResponse> listWithCategoryNPlusOne(int page, int size, String sort, Long categoryIdx) {
        Sort sortOption = getSortOption(sort);
        Pageable pageable = PageRequest.of(page, size, sortOption);

        // ✅ JPA에서 페이징된 `Page<Products>` 가져오기 (N+1 발생 가능)
        Page<Products> productsPage = productsRepository.findByCategory_Idx(categoryIdx, pageable);

        // ✅ `Page<Products>` → `Page<ProductsDto.InfoResponse>` 변환
        Page<ProductsDto.InfoResponse> dtoPage = productsPage.map(ProductsDto.InfoResponse::fromEntity);

        return dtoPage;
    }


    // 정렬 처리 (Spring Data JPA Sort 적용)
    private Sort getSortOption(String sort) {
        if (sort == null || sort.isEmpty()) {
            return Sort.by(Sort.Direction.DESC, "idx"); // 기본 정렬 (최신순)
        }

        return switch (sort) {
            case "reviewCount" -> Sort.by(Sort.Direction.DESC, "reviewCount");
            case "starPoint" -> Sort.by(Sort.Direction.DESC, "starPoint");
            default -> Sort.by(Sort.Direction.ASC, "idx"); // 기본 정렬 idx ASC
        };
    }


    // 리뷰 starPoint, 리뷰 개수, 이미지
    public List<ProductsDto.InfoResponse> list() {
        Page<Products> products = productsRepository.findAll(PageRequest.of(0, 6));
        return products.getContent().stream().map(ProductsDto.InfoResponse::fromEntity).collect(Collectors.toList());
    }

    public ProductsDto.DetailResponse getProduct(Long id) {
        Products products = productsRepository.findByIdxWithReviewsAndImages(id).orElseThrow();
        return ProductsDto.DetailResponse.fromEntity(products);
    }

    public List<ProductReviewsDto.InfoResponse> getProductReviews(Long idx) {
        Products products = productsRepository.findByIdx(idx);
        return products.getReviews().stream().map(ProductReviewsDto.InfoResponse::fromEntity).collect(Collectors.toList());
    }

    public void addReviewCnt(Long productIdx) {
        Products products = productsRepository.findByIdx(productIdx);
        products.addReviewCount();
        productsRepository.save(products);
    }

    public void calculateStarPoint(Long productIdx, int starPoint) {
        Products products = productsRepository.findByIdx(productIdx);
        products.calculateStarPoint(starPoint);
        productsRepository.save(products);
    }

    public Page<Products> getSearchList(int page, int size, String keyword) {
        return productsRepository.findByNameContaining(keyword,PageRequest.of(page, size));
    }
}
