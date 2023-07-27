package com.renaissance.orderservice.service;

import com.renaissance.orderservice.dto.OrderRequest;
import com.renaissance.orderservice.dto.OrderResponse;

import java.util.List;
import java.util.Optional;

public interface OrderService {
     void placeOrder(OrderRequest orderRequest);
     List<OrderResponse> getAllOrders();

     Optional<OrderResponse> getOrderById(long id);
}
