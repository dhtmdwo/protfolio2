package com.example.appapi.product.model;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum SortType {
    REVIEW_COUNT("reviewcount", Order.DESC, "reviewCount", Integer.class),
    STAR_POINT("starpoint", Order.DESC, "starPoint", Double.class),
    DEFAULT("idx", Order.ASC, "idx", Long.class);

    private final String value;
    private final Order order;
    private final String field;
    private final Class<?> fieldType;

    SortType(String value, Order order, String field, Class<?> fieldType) {
        this.value = value;
        this.order = order;
        this.field = field;
        this.fieldType = fieldType;
    }

    public static SortType fromString(String sort) {
        if (sort == null) return DEFAULT;
        for (SortType type : values()) {
            if (type.value.equalsIgnoreCase(sort)) {
                return type;
            }
        }
        return DEFAULT; // 기본값 설정
    }

    public OrderSpecifier<?> getOrderSpecifier() {
        PathBuilder<Products> pathBuilder = new PathBuilder<>(Products.class, "products"); // ✅ 올바른 alias 설정
        return new OrderSpecifier<>(order, pathBuilder.get(field, (Class) fieldType)); // ✅ (Class)로 명시적 캐스팅 추가
    }
}