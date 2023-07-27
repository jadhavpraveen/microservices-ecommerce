package com.renaissance.inventoryservice.service;


import com.renaissance.inventoryservice.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.renaissance.inventoryservice.repository.InventoryRepository;

import java.util.List;

@Service
@RequiredArgsConstructor

public class InventoryServiceImpl implements InventoryService{

    private final InventoryRepository inventoryRepository;


    @Transactional(readOnly = true)
    @Override
    public List<InventoryResponse> isInStock(List<String> skuCode)    {

        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }
}
