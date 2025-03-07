package com.example.appapi.menus.model;

import com.example.appapi.store.model.Store;
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
public class Menus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String name;
    private int price;
    private String info;

    @ManyToOne
    @JoinColumn(name = "store_idx")
        private Store store;  // user_idx (FK)
}
