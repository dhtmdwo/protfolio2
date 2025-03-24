package com.example.appapi.orders;

import com.example.appapi.delivery.model.Delivery;
import com.example.appapi.orders.orderProducts.model.OrderProducts;
import com.example.appapi.orders.orderProducts.model.OrderProductsDto;
import com.example.appapi.orders.model.Orders;
import com.example.appapi.orders.model.OrdersDto;
import com.example.appapi.payment.model.Payment;
import com.example.appapi.product.model.Products;
import com.example.appapi.users.model.Users;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrdersService {
    private final OrdersRepository ordersRepository;
    @Transactional
    public OrdersDto.OrdersRegisterResponse register(Long idx, OrdersDto.OrdersRegister dto) {
        ordersRepository.updateOrderMessageAndStatus(idx, dto.getMessage());
        Orders order = ordersRepository.findByIdx(idx);
        return OrdersDto.OrdersRegisterResponse.from(order);
    }

    public List<OrdersDto.OrdersResponse> getOrderList(Long userIdx) {
        List<Orders> ordersList = ordersRepository.findAllWithOrderProductsAndProducts(userIdx);
        List<OrdersDto.OrdersResponse> ordersResponseList = new ArrayList<>();

        for (Orders order : ordersList) {
            List<OrderProductsDto.OrderProductResponse> orderProductResponses = new ArrayList<>();

            for (OrderProducts orderProduct : order.getOrderProducts()) {
                Products products = orderProduct.getProducts();
                OrderProductsDto.ProductsResponse productResponse = OrderProductsDto.ProductsResponse.of(products);
                orderProductResponses.add(OrderProductsDto.OrderProductResponse.of(orderProduct, productResponse));
            }
            OrdersDto.OrdersResponse ordersResponse = OrdersDto.OrdersResponse.from(order, orderProductResponses);
            ordersResponseList.add(ordersResponse);
        }
        return ordersResponseList;
    }

    public List<OrdersDto.OrderMypageList> orderList(Long idx) {
        List<Orders> orders = ordersRepository.findMyAllOrders(idx);
        List<OrdersDto.OrderMypageList> responseList = new ArrayList<>();

        for (Orders order : orders) {
            List<OrderProducts> orderProductList = order.getOrderProducts();
            List<OrdersDto.MyOrder> myOrderList = new ArrayList<>();
            for(OrderProducts orderProduct : orderProductList) {
                Products products = orderProduct.getProducts();
                String productName = products.getName(); // 상품
                String image = Optional.ofNullable(products.getImages())
                        .filter(images -> !images.isEmpty())  // 이미지 리스트가 비어있지 않은 경우
                        .map(images -> images.get(0).getImagePath())  // 첫 번째 이미지 경로 가져오기
                        .orElse(null);  // 없으면 null 반환
                int quantity = orderProduct.getQuantity(); // 상품 수량
                myOrderList.add(OrdersDto.MyOrder.from(productName, quantity, image));
            }
            responseList.add(OrdersDto.OrderMypageList.from(order, myOrderList));
        }

        return responseList;
    } // 마이페이지 클라이언트 주문 리스트 보기

    public OrdersDto.OrderMypageDetails orderDetails(Long userIdx, Long orderIdx) {
        Orders orders = ordersRepository.findOrderWithDetails(userIdx, orderIdx);
        
        List<OrderProducts> orderProductList = orders.getOrderProducts();
        List<OrdersDto.MyOrder> myOrderList = new ArrayList<>(); // 주문 리스트
        for(OrderProducts orderProduct : orderProductList) {
            Products products = orderProduct.getProducts();
            String productName = products.getName(); // 상품
            String image = Optional.ofNullable(products.getImages())
                    .filter(images -> !images.isEmpty())  // 이미지 리스트가 비어있지 않은 경우
                    .map(images -> images.get(0).getImagePath())  // 첫 번째 이미지 경로 가져오기
                    .orElse(null);  // 없으면 null 반환
            int quantity = orderProduct.getQuantity(); // 상품 수량
            myOrderList.add(OrdersDto.MyOrder.from(productName, quantity, image));
        } // 상품 이름, 개수 리스트 넣기

        OrdersDto.OrderMypageList orderMypage = OrdersDto.OrderMypageList.from(orders, myOrderList); // mypageList
        Users users = orders.getUser();
        Payment payment = orders.getPayments().get(0);
        Delivery delivery = orders.getDelivery();
        OrdersDto.OrderMypageDetails orderMypageDetails = OrdersDto.OrderMypageDetails.from(orderMypage, orders, users, payment, delivery);

        return orderMypageDetails;
    } // 마이페이지 클라이언트 주문 상세 보기
    


    public OrdersDto.OrdersResponse getOrderRead(Long orderIdx) {
        Optional<Orders> order = ordersRepository.findByIdWithOrderProductsAndProducts(orderIdx);

        if (order.isEmpty()) {
            return null;
        }
        List<OrderProductsDto.OrderProductResponse> orderProductResponses = new ArrayList<>();
        for (OrderProducts orderProduct : order.get().getOrderProducts()) {
            Products products = orderProduct.getProducts();
            OrderProductsDto.ProductsResponse productResponse = OrderProductsDto.ProductsResponse.of(products);
            orderProductResponses.add(OrderProductsDto.OrderProductResponse.of(orderProduct, productResponse));
        }
        return OrdersDto.OrdersResponse.from(order.get(), orderProductResponses);
    }

}
