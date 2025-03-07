package com.example.resv.resv.model;

import com.example.appapi.store.model.Store;
import com.example.appapi.store.review.model.StoreReview;
import com.example.appapi.users.model.Users;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Resv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate date;
    private LocalTime time;
    private String name;
    private int headCount;
    private String request;
    private String isReviewed;


    @ManyToOne
    @JoinColumn(name = "store_idx")
    private Store store;        // store 연관

    @ManyToOne
    @JoinColumn(name = "user_idx")
    private Users user;        // user 연관

}