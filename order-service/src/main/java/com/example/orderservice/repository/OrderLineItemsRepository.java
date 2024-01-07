package com.example.orderservice.repository;

import com.example.orderservice.model.OrderLineItems;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineItemsRepository extends JpaRepository<OrderLineItems, Long> {
}
