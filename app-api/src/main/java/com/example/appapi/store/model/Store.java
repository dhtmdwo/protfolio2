package com.example.appapi.store.model;

import com.example.appapi.category.model.Category;
import com.example.appapi.likes.model.Likes;
import com.example.appapi.store.images.model.StoreImages;
import com.example.appapi.store.review.model.StoreReview;
import com.example.appapi.users.model.Users;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String name;
    private String description;

    private String address;
    private String shortAddress;
    private String callNumber;
    private String openingHours;

    private LocalTime startTime;
    private LocalTime endTime;

    private Long likesCount;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AllowedStatus allowed;  // ENUM ('Yes', 'No', 'Waiting')

    @OneToMany(mappedBy = "store")
    private List<StoreClosedDay> closedDayList = new ArrayList<>();  // store_closed_days (FK)

    //@OneToMany
    //private List<Resv> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Likes> likesList = new ArrayList<>();

    @BatchSize(size = 4)
    @OneToMany(mappedBy = "store")
    private List<StoreImages> images;

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private Users user;  // user_idx (FK)

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_idx")
    private Category category;  // category_idx (FK)

    @OneToMany(mappedBy = "store")
    private List<StoreReview> storeReviewList;

    public void addLike() {
        this.likesCount++;
    }

    public void subLike() {
        if (this.likesCount > 0) {
            this.likesCount--;
        }
    }

    private int reviewCnt;
    private double starPoint;

    public void updateReviewStatus() {
        if (storeReviewList.isEmpty()) {
            this.reviewCnt = 0;
            this.starPoint = 0.0;
        } else {
            this.reviewCnt = storeReviewList.size();
            this.starPoint = storeReviewList.stream()
                    .mapToDouble(StoreReview::getStarPoint)
                    .average()
                    .orElse(0.0);
        }
    }


}
