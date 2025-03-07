package com.example.appapi.product.model;

import com.example.appapi.category.model.Category;
import com.example.appapi.product.images.model.ProductsImages;
import com.example.appapi.product.review.model.ProductReviews;
import com.example.appapi.store.model.Store;
import com.example.appapi.users.model.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Products {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    private String name;
    private String description;
    private int price;
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_idx")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_idx",nullable = true)
    private Store store;

//    @BatchSize(size = 6)
    @OneToMany(mappedBy = "products")
    private List<ProductsImages> images;

//    @BatchSize(size = 6)
    @OneToMany(mappedBy = "products")
    private List<ProductReviews> reviews;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_idx")
    private Category category;

    private int reviewCount;

    public void addReviewCount() {
        this.reviewCount = this.reviewCount + 1;
    }

    public void subReviewCount() {
        this.reviewCount = this.reviewCount - 1;
    }

    private double starPoint;

    public void calculateStarPoint(int value) {
        if (totalStarCount == 0) {
            starPoint = value; // 첫 번째 리뷰일 때는 그대로 반영
        } else {
            starPoint = (starPoint * totalStarCount + value) / (totalStarCount + 1);
        }
        addStarCount(); // ⭐ 카운트 증가를 항상 보장
    }

    private double totalStarCount;

    public void addStarCount() {
        this.totalStarCount = this.totalStarCount + 1;
    }

    public void subStarCount() {
        this.totalStarCount = this.totalStarCount - 1;
    }
}
