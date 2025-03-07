package com.example.appapi.product;

import com.example.appapi.category.model.QCategory;
import com.example.appapi.product.images.model.QProductsImages;
import com.example.appapi.product.model.Products;
import com.example.appapi.product.model.ProductsDto;
import com.example.appapi.product.model.QProducts;
import com.example.appapi.store.model.Store;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductQueryRepository {
    private final JPAQueryFactory queryFactory;
    private final QProducts products;
    private final QCategory category;
    private final QProductsImages productsImages;

    public ProductQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.products = QProducts.products;
        this.category = QCategory.category;
        this.productsImages = QProductsImages.productsImages;
    }

    public Page<ProductsDto.InfoResponse> search(int page, int size, String sort, Long categoryIdx) {
        BooleanBuilder builder = new BooleanBuilder();
        // 정렬 설정
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort);

        if(categoryIdx != null) {
            builder.and(products.category.idx.eq(categoryIdx));
        }

        List<Products> productsList = queryFactory.selectFrom(products)
                .join(products.category, category) // ✅ 올바른 조인 (products → category)
                .join(products.images, productsImages) // ✅ products → images 조인 추가
                .where(builder)
                .groupBy(products.idx) // ✅ 중복 제거
                .orderBy(orderSpecifier)
                .offset((long) page * size)
                .limit(size)
                .fetch();

        List<ProductsDto.InfoResponse> dtoList = productsList.stream().map(ProductsDto.InfoResponse::fromEntity).collect(Collectors.toList());


        // 전체 개수 조회
        long total = Optional.ofNullable(
                queryFactory.select(products.idx.countDistinct()) // ✅ 중복 방지
                        .from(products)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(dtoList, PageRequest.of(page, size), total);
    }


    // 정렬 처리
    private OrderSpecifier<?> getOrderSpecifier(String sort) {
        PathBuilder<Products> pathBuilder = new PathBuilder<>(Products.class, "store");

        if (sort == null) {
            sort = "idx";
        }

        return switch (sort.toLowerCase()) {
            case "reviewcount" -> new OrderSpecifier<>(Order.DESC, pathBuilder.getNumber("reviewCount", Integer.class));
            case "starpoint" -> new OrderSpecifier<>(Order.DESC, pathBuilder.getNumber("starPoint", Double.class));
            default -> new OrderSpecifier<>(Order.ASC, pathBuilder.getNumber("idx", Long.class)); // 기본 정렬 idx ASC
        };
    }
}
