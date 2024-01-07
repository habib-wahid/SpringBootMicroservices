package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final WebClient.Builder webClientBuilder;

  public void placeOrder(OrderRequest orderRequest) {
    Order order = Order.builder()
      .orderNumber(UUID.randomUUID().toString())
      .orderLineItemsList(
        orderRequest
          .getOrderLineItemsDtoList()
          .stream().map(this::mapToOrderLineItems).toList())
      .build();
    List<String> skuCodes = orderRequest.getOrderLineItemsDtoList().stream()
      .map(OrderLineItemsDto::getSkuCode)
      .toList();
    InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
      .uri("http://inventory-service/api/inventory",
        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
      .retrieve()
      .bodyToMono(InventoryResponse[].class)
      .block();

    boolean allProductInStock = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::getIsInStock);
    if(allProductInStock){
      orderRepository.save(order);
    }else{
      throw new IllegalArgumentException("Product is not in stock, please try again latter");
    }

  }

  private OrderLineItems mapToOrderLineItems(OrderLineItemsDto orderLineItemsDto) {
    return OrderLineItems
      .builder()
      .price(orderLineItemsDto.getPrice())
      .skuCode(orderLineItemsDto.getSkuCode())
      .build();
  }
}
