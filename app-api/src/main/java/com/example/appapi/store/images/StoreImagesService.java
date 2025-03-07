package com.example.appapi.store.images;

import com.example.appapi.product.images.model.ProductsImages;
import com.example.appapi.product.images.repository.ProductsImagesRepository;
import com.example.appapi.product.model.Products;
import com.example.appapi.store.images.model.StoreImages;
import com.example.appapi.store.images.model.StoreImagesRepository;
import com.example.appapi.store.model.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreImagesService {
    private final StoreImagesRepository storeImagesRepository;

    public List<String> getAllStoreImages() {
        return storeImagesRepository.findAll().stream().map(image -> image.getImagePath()).collect(Collectors.toList());
    }

    public void create(List<String> uploadFilePaths, Store store) {
        for(String uploadFilePath: uploadFilePaths) {

            storeImagesRepository.save(
                    StoreImages.builder()
                            .imagePath(uploadFilePath)
                            .store(store)
                            .build())
            ;
        }
    }
}
