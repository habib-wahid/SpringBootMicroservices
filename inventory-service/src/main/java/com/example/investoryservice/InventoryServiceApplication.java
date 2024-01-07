package com.example.investoryservice;

import com.example.investoryservice.model.Inventory;
import com.example.investoryservice.repository.InventoryRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(InventoryServiceApplication.class, args);
  }

  @Bean
  public CommandLineRunner loadData(InventoryRepository inventoryRepository){
    return args -> {
      Inventory inventory1 = Inventory
        .builder()
        .skuCode("iPhone_13")
        .quantity(100)
        .build();

      Inventory inventory2 = Inventory
        .builder()
        .skuCode("iPhone_13_red")
        .quantity(1000)
        .build();

      inventoryRepository.save(inventory1);
      inventoryRepository.save(inventory2);
    };
  }
}
