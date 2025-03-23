package com.example.resv;


import com.example.appapi.orders.OrdersRepository;
import com.example.appapi.orders.model.Orders;
import com.example.appapi.orders.orderProducts.OrderProductsRepository;
import com.example.appapi.orders.orderProducts.model.OrderProducts;
import com.example.appapi.paymentmethod.model.PaymentMethod;
import com.example.appapi.product.images.model.ProductsImages;
import com.example.appapi.product.images.repository.ProductsImagesRepository;
import com.example.appapi.product.model.Products;
import com.example.appapi.product.repository.ProductsRepository;
import com.example.appapi.product.review.images.model.ProductReviewImages;
import com.example.appapi.product.review.images.repository.ProductReviewImagesRepository;
import com.example.appapi.product.review.model.ProductReviews;
import com.example.appapi.product.review.repository.ProductReviewsRepository;
import com.example.appapi.users.UsersRepository;
import com.example.appapi.users.model.UserType;
import com.example.appapi.users.model.Users;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final UsersRepository usersRepository;
    private final ProductsRepository productsRepository;
    private final ProductReviewsRepository productReviewsRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProductsImagesRepository productsImagesRepository;
    private final ProductReviewImagesRepository productReviewImagesRepository;
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;

    @PostConstruct
    public void dataInsert() {
        PaymentMethod easyMethod = PaymentMethod.builder()
                .name("EASY_PAY")
                .build();


        for (int i = 1; i <= 3; i++) {
            Users user = Users.builder()
                    .email("test0" + i + "@test.com")
                    .password(passwordEncoder.encode("qwer1234"))
                    .phone("123456789")
                    .address("test address")
                    .userId("test0" + i)
                    .addressDetail("test address detail")
                    .birthDate("test")
                    .name("test0"+i)
                    .userType(UserType.SELLER)
                    .build();

            usersRepository.save(user);

            for (int j = 1; j <= 6; j++) {
                Products products = productsRepository.save(Products.builder()
                        .user(user)
                        .name("상품-" + (j + 5 * (i - 1)))
                        .stock(10)
                                .price(j * 10000)
                        .description("상품-" + (j + 5 * (i - 1)) + " by test0" + i + "@test.com")
                        .build());
                productsImagesRepository.save(
                        ProductsImages.builder()
                                .products(products)
                                .imagePath("https://godomall.speedycdn.net/ec5d2a1c8483712efb957784c858b320/goods/1000000463/image/detail/1000000463_detail_020.jpg")
                                .build()
                );
            }
        }


        for(int i=1;i<=3;i++){
            Users user = usersRepository.findById(Long.valueOf(i)).get();
            Orders orders = Orders.builder()
                    .user(user)
                    .price(i * 10000)
                    .message("테스트 주문")
                    .status("결제 대기")
                    .build();

            ordersRepository.save(orders);

            Products products = productsRepository.findByIdx(Long.valueOf(i));
            OrderProducts orderProducts = OrderProducts.builder()
                    .quantity(1)
                    .orders(orders)
                    .products(products)
                    .build();
            orderProductsRepository.save(orderProducts);

            for (int j = 1; j <= 6; j++) {
                ProductReviews productReviews = ProductReviews.builder()
                        .products(Products.builder()
                                .idx(Long.valueOf(j))
                                .build())
                        .content("상품" + j + "에 대한 리뷰")
                        .user(Users.builder()
                                .idx(Long.valueOf(i))
                                .build())
                        .build();

                ProductReviews save = productReviewsRepository.save(productReviews);
                ProductReviewImages savedImages = ProductReviewImages.builder()
                        .imagePath("https://d12zq4w4guyljn.cloudfront.net/20240925123559_photo1_bb072b2e6070.jpg")
                        .productReviews(save)
                        .build();
                productReviewImagesRepository.save(savedImages);
            }
        }


    }
}
