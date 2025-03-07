package com.example.appapi.store.review.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class StoreReviewImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeReview_idx")
    private StoreReview storeReview;
}