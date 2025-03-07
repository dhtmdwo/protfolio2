package com.example.appapi.product.review.model;

import com.example.appapi.orders.orderProducts.model.OrderProducts;
import com.example.appapi.product.model.Products;
import com.example.appapi.product.review.images.model.ProductReviewImages;
import com.example.appapi.users.model.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_reviews")
public class ProductReviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String title;
    private int starPoint;
    private String content;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "products_idx")
    private Products products;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_idx")
    private Users user;

    @OneToMany(mappedBy = "productReviews", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProductReviewImages> images;

    @OneToOne
    @JoinColumn(name = "orderProduct_idx")
    private OrderProducts orderProducts;
}
