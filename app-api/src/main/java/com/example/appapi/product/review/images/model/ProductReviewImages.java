package com.example.appapi.product.review.images.model;

import com.example.appapi.product.review.model.ProductReviews;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_review_images")
public class ProductReviewImages {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_reviews_idx",nullable = true)
    private ProductReviews productReviews;
}
