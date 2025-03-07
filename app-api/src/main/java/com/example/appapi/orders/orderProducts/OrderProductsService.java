package com.example.appapi.orders.orderProducts;

import com.example.appapi.orders.orderProducts.model.OrderProducts;
import com.example.appapi.orders.orderProducts.model.OrderProductsDto;
import com.example.appapi.orders.OrdersRepository;
import com.example.appapi.orders.model.Orders;
import com.example.appapi.product.model.Products;
import com.example.appapi.product.repository.ProductsRepository;
import com.example.appapi.users.model.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderProductsService {
    private final OrdersRepository ordersRepository;
    private final OrderProductsRepository orderProductsRepository;
    private final ProductsRepository productsRepository;
    @Transactional
    public OrderProductsDto.OrderRegisterResponse register(OrderProductsDto.OrderRegisterRequest orderRequest, Users user) {
        Orders order = Orders.builder()
                .user(user)
                .status("Pending")
                .message("")
                .build();
        Orders savedOrder = ordersRepository.save(order);

        int totalPrice = 0;
        List<OrderProducts> orderProductsList = new ArrayList<>();

        for (OrderProductsDto.OrderProductRegisterRequest orderProductDto : orderRequest.getOrderProductRegisterRequest()) {
            Products product = productsRepository.findByIdx(orderProductDto.getProductIdx());
            if (product == null) {
                throw new IllegalArgumentException("존재하지 않는 상품: " + orderProductDto.getProductIdx());
            }
            int quantity = orderProductDto.getQuantity();
            int productPrice = product.getPrice() * quantity;
            totalPrice += productPrice;

            OrderProducts orderProduct = orderProductDto.toEntity(savedOrder, product);
            orderProductsList.add(orderProduct);
        }
        orderProductsRepository.saveAll(orderProductsList);
        if (totalPrice != orderRequest.getTotalPrice()) {
            throw new IllegalStateException("가격 불일치: " + totalPrice + " ≠ " + orderRequest.getTotalPrice());
        }
        ordersRepository.updateOrderPrice(savedOrder.getIdx(), totalPrice);
        Orders findOrder = ordersRepository.findByIdx(savedOrder.getIdx());
        return OrderProductsDto.OrderRegisterResponse.from(findOrder, totalPrice);
    }


    public List<OrderProductsDto.ListProductsResponse> list(Long ordersIdx) {
        List<OrderProducts> result = orderProductsRepository.findAllByOrdersIdx(ordersIdx);
        List<OrderProductsDto.ListProductsResponse> dtoList = new ArrayList<>();
        for (OrderProducts orderProducts : result) {
            Products product = orderProducts.getProducts();
            OrderProductsDto.ListProductResponse dto = OrderProductsDto.ListProductResponse.from(product);
            OrderProductsDto.ListProductsResponse resp = OrderProductsDto.ListProductsResponse.from(orderProducts, dto);
            dtoList.add(resp);
        }
        return dtoList;
    }
}
