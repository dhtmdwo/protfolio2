package com.example.appapi.category;

import com.example.appapi.category.model.Category;
import com.example.appapi.category.model.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;


    @Transactional
    public List<CategoryDto.CategoryResponseDto> getBigCategory() {
        List<Category> categoryList = categoryRepository.findRootCategoriesWithChildren();

        return categoryList.stream().map(CategoryDto.CategoryResponseDto::from).toList();
    }
}
