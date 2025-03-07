package com.example.appapi.store.review.model;

import com.example.appapi.store.model.Store;
import com.example.appapi.users.model.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class StoreReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String title;
    private String contents;
    private int starPoint;
    @Column(nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime createdAt;

    // 저장될 때 자동으로 현재 시간 설정
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    @OneToMany(mappedBy = "storeReview", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<StoreReviewImage> storeReviewImageList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "store_idx")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "users_idx")
    private Users users;

}