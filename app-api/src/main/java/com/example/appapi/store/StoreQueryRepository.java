package com.example.appapi.store;

import com.example.appapi.category.model.QCategory;
import com.example.appapi.store.images.model.QStoreImages;
import com.example.appapi.store.model.*;
import com.example.appapi.users.model.QUsers;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class StoreQueryRepository {
    private final JPAQueryFactory queryFactory;
    private QStore store;
    private QCategory category;
    private QStoreImages storeImages;
    private QStoreClosedDay storeClosedDay;
    private QUsers users;


    public StoreQueryRepository(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.store = QStore.store;
        this.category = QCategory.category;
        this.storeImages = QStoreImages.storeImages;
        this.storeClosedDay = QStoreClosedDay.storeClosedDay;
        this.users = QUsers.users;
    }

    public Page<Store> searchWithAllowed(int page, int size, String allowed) {
        BooleanBuilder builder = new BooleanBuilder();
        if (allowed != null) {
            builder.and(store.allowed.eq(AllowedStatus.valueOf(allowed)));
        }

        List<Store> storeList = queryFactory
                .selectFrom(store)
                .where(builder)
                .offset((long) page * size)
                .limit(size)
                .fetch();

        long total = Optional.ofNullable(
                queryFactory.select(store.idx.countDistinct()) // ✅ 중복 방지
                        .from(store)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);
        return new PageImpl<>(storeList, PageRequest.of(page, size), total);
    }

    public Page<Store> search(int page, int size, String sort, Long categoryIdx) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(store.allowed.eq(AllowedStatus.YES));

        if (categoryIdx != null) {
            builder.and(store.category.idx.eq(categoryIdx));
        }

        // 정렬 설정
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sort);

        // 데이터 조회
        List<Store> storeList = queryFactory
                .selectFrom(store)
                .leftJoin(store.category, category)
                .leftJoin(store.images, storeImages)
                .leftJoin(store.closedDayList, storeClosedDay)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset((long) page * size)
                .limit(size)
                .fetch();

        // 전체 개수 조회
        long total = Optional.ofNullable(
                queryFactory.select(store.idx.countDistinct()) // ✅ 중복 방지
                        .from(store)
                        .where(builder)
                        .fetchOne()
        ).orElse(0L);

        return new PageImpl<>(storeList, PageRequest.of(page, size), total);
    }

    // 정렬 처리
    private OrderSpecifier<?> getOrderSpecifier(String sort) {
        PathBuilder<Store> pathBuilder = new PathBuilder<>(Store.class, "store");

        if (sort == null) {
            sort = "idx";
        }

        return switch (sort.toLowerCase()) {
            case "likecount" -> new OrderSpecifier<>(Order.DESC, pathBuilder.getNumber("likeCount", Integer.class));
            case "reviewcount" -> new OrderSpecifier<>(Order.DESC, pathBuilder.getNumber("reviewCount", Integer.class));
            case "starpoint" -> new OrderSpecifier<>(Order.DESC, pathBuilder.getNumber("starPoint", Double.class));
            default -> new OrderSpecifier<>(Order.ASC, pathBuilder.getNumber("idx", Long.class)); // 기본 정렬 idx ASC
        };
    }
}