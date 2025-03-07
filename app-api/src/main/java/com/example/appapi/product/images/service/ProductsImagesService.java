package com.example.appapi.product.images.service;

import com.example.appapi.product.images.model.ProductsImages;
import com.example.appapi.product.images.repository.ProductsImagesRepository;
import com.example.appapi.product.model.Products;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductsImagesService {
    private final ProductsImagesRepository productsImagesRepository;

    public List<String> getAllProductsImages() {
        return productsImagesRepository.findAll().stream().map(image -> image.getImagePath()).collect(Collectors.toList());
    }

    public void create(List<String> uploadFilePaths, Products product) {
        for(String uploadFilePath: uploadFilePaths) {

            productsImagesRepository.save(
                    ProductsImages.builder()
                            .imagePath(uploadFilePath)
                            .products(product)
                            .build())
            ;
        }
    }
}
