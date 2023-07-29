package com.renaissance.inventoryservice;

import com.renaissance.inventoryservice.model.Inventory;
import com.renaissance.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class InventoryServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class,args);
    }
//        @Bean
//        public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
//            return args -> {
//                Inventory inventory = new Inventory();
//                inventory.setQuantity(200);
//                inventory.setSkuCode("iphone_13");
//
//
//                Inventory inventory1 = new Inventory();
//                inventory.setQuantity(0);
//                inventory.setSkuCode("iphone_13_red");
//                inventoryRepository.save(inventory);
//                inventoryRepository.save(inventory1);

//                List<Inventory> inventoryList = new ArrayList<>();
//                inventoryList.add(inventory);
//                inventoryList.add(inventory1);
//                        Arrays.asList(
//                        new Inventory("iphone_13", 200),
//                        new Inventory( "iphone_13_red", 0)
////                        new Inventory("iphone_13", 200),
////                        new Inventory(  0, "iphone_13_red")
//
//                ));
//
                //inventoryRepository.saveAll(inventoryList);

//            };
//        }
}