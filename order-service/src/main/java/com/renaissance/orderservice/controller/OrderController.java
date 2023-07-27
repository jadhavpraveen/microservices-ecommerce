package com.renaissance.orderservice.controller;

import com.renaissance.orderservice.dto.OrderRequest;
import com.renaissance.orderservice.dto.OrderResponse;
import com.renaissance.orderservice.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {
    private final OrderServiceImpl orderService;
    @PostMapping
    public ResponseEntity<OrderRequest> placeOrder(@RequestBody OrderRequest orderRequest)    {
        orderService.placeOrder(orderRequest);
        return new ResponseEntity<>(orderRequest, HttpStatus.CREATED);
    }
    @GetMapping("orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders()   {
        List<OrderResponse> orderResponseList = orderService.getAllOrders();
        return new ResponseEntity<>(orderResponseList,HttpStatus.OK);
    }
    @GetMapping("orders/{orderId}")
    public ResponseEntity<OrderResponse> getOrderByOrderId(@PathVariable(value = "orderId") long id)   {
        Optional<OrderResponse> orderResponse = orderService.getOrderById(id);
        return new ResponseEntity<>(orderResponse.get(),HttpStatus.OK);
    }
}
