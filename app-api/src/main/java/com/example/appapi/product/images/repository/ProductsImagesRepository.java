package com.example.appapi.product.images.repository;

import com.example.appapi.product.images.model.ProductsImages;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductsImagesRepository extends JpaRepository<ProductsImages, Long> {
}
