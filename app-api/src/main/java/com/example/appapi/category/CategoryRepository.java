package com.example.appapi.category;

import com.example.appapi.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c " +
            "LEFT JOIN FETCH c.childCategoryList cl" +
            " WHERE c.parentCategory IS NULL")
    List<Category> findRootCategoriesWithChildren();


    List<Category> findByParentCategory_Idx(Long categoryIdx);

    Optional<Category> findByName(String name);
}
