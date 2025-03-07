package com.example.appapi.category.model;

import com.example.appapi.store.model.Store;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Setter
    private String name;

    // 자기 참조 관계 (부모 카테고리)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_idx", nullable = true)
    private Category parentCategory;

    @Builder.Default
    // 자식 카테고리 목록
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> childCategoryList  = new ArrayList<>();

    @BatchSize(size = 10)
    @OneToMany(mappedBy = "category")
    private List<Store> storeList = new ArrayList<>();
}