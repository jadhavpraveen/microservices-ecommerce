package com.renaissance.orderservice.service;

import com.renaissance.orderservice.dto.InventoryResponse;
import com.renaissance.orderservice.dto.OrderLineItemsDto;
import com.renaissance.orderservice.dto.OrderRequest;
import com.renaissance.orderservice.dto.OrderResponse;
import com.renaissance.orderservice.exception.ResourceNotFoundException;
import com.renaissance.orderservice.model.Order;
import com.renaissance.orderservice.model.OrderLineItems;
import com.renaissance.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    @Override
    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtosList()
                        .stream()
                        .map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList();
        order.setOrderLineItemsList(orderLineItemsList);

       List<String> skuCodes =  order.getOrderLineItemsList().stream().map(orderLineItem -> orderLineItem.getSkuCode() ).toList();
        //call inventory service and place order if product is in inventory stock
        InventoryResponse[] inventoryResponses = webClient.get()
                                  .uri("http://localhost:8082/api/inventory",
                                          uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                                  .retrieve()
                                  .bodyToMono(InventoryResponse[].class)
                                  .block();                          // block makes the synchronous call
        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);
        if(allProductsInStock) {
            orderRepository.save(order);
            log.info("Order placed successfully");
        }
        else throw new IllegalArgumentException("Product is not in stock, please try again later");
    }
    @Override
    public List<OrderResponse> getAllOrders()   {
        List<Order> orderList = orderRepository.findAll();
        return  orderList.stream()
                .map(this::mapToOrderResponse).toList();
    }
    @Override
    public Optional<OrderResponse> getOrderById(long id) {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()) {
            log.info("Order details not found for Order Id : {} " , id);
            throw new ResourceNotFoundException("Order details not found for Order Id : " + id);
        }
        return Optional.ofNullable(mapToOrderResponse(order.get()));
    }
    private OrderResponse mapToOrderResponse(Order order) {
      List<OrderLineItemsDto> orderLineItemsDtos = order.getOrderLineItemsList()
                .stream().map(o -> mapOrderLineItemsToDto(o)).toList();

        OrderResponse orderResponse =  OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .orderLineItemsDtosList((orderLineItemsDtos))
                .build();
          return orderResponse;
    }
    private OrderLineItemsDto mapOrderLineItemsToDto(OrderLineItems orderLineItems) {
        OrderLineItemsDto orderLineItemsDto = new OrderLineItemsDto();
        orderLineItemsDto.setPrice(orderLineItems.getPrice());
        orderLineItemsDto.setSkuCode(orderLineItems.getSkuCode());
        orderLineItemsDto.setQuantity(orderLineItems.getQuantity());
        return orderLineItemsDto;
    }
    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto)    {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity((orderLineItemsDto.getQuantity()));
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
